package com.Hofhofo8.Qogfad.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Hofhofo8.Qogfad.core.config.GameConfig
import com.Hofhofo8.Qogfad.data.sample.SampleData
import com.Hofhofo8.Qogfad.domain.model.BlockCard
import com.Hofhofo8.Qogfad.domain.model.BlockFamily
import com.Hofhofo8.Qogfad.domain.model.Flaw
import com.Hofhofo8.Qogfad.domain.model.Floor
import com.Hofhofo8.Qogfad.domain.model.Raider
import com.Hofhofo8.Qogfad.domain.model.RunOutcome
import com.Hofhofo8.Qogfad.domain.model.RunResult
import com.Hofhofo8.Qogfad.domain.model.Stage
import com.Hofhofo8.Qogfad.domain.model.StageId
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import com.Hofhofo8.Qogfad.domain.repository.SettingsRepository
import com.Hofhofo8.Qogfad.domain.usecase.ApplyBlockUseCase
import com.Hofhofo8.Qogfad.domain.usecase.EvaluateRunUseCase
import com.Hofhofo8.Qogfad.domain.usecase.GenerateTrioUseCase
import com.Hofhofo8.Qogfad.domain.usecase.ResolveRaiderUseCase
import com.Hofhofo8.Qogfad.domain.usecase.RunTally
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generateTrio: GenerateTrioUseCase,
    private val applyBlock: ApplyBlockUseCase,
    private val resolveRaider: ResolveRaiderUseCase,
    private val evaluateRun: EvaluateRunUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<RunResult>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var loop: Job? = null
    private var activeRun = -1
    private var phaseElapsed = 0L
    private var phaseBudget = GameConfig.PACE_STANDARD_MS
    private var bannerElapsed = 0L
    private var nextFloorId = 0
    private var nextRaiderId = 0
    private var waveRandom = Random(1)
    private var stage: Stage = SampleData.stages.first()
    private var trueBlocks = 0
    private var familyDrops = mutableMapOf<BlockFamily, Int>()
    private var finishing = false

    fun beginRun(runId: Int) {
        if (activeRun == runId) {
            if (!finishing && loop == null) restartLoop()
            return
        }
        activeRun = runId
        finishing = false
        trueBlocks = 0
        familyDrops = mutableMapOf()
        nextFloorId = 0
        nextRaiderId = 0

        val progress = progressRepository.progress.value
        val settings = settingsRepository.settings.value
        stage = SampleData.stageOf(progress.activeStage)
        phaseBudget = settings.paceMs
        phaseElapsed = 0L
        bannerElapsed = 0L
        waveRandom = seedFor(1, runId)

        val foundation = List(GameConfig.START_FLOORS) {
            Floor(
                id = nextFloorId++,
                family = stage.crestFamily,
                width = 2,
                offsetX = 0,
                armor = 1,
                integrity = 100
            )
        }

        _state.value = GameUiState(
            phase = GamePhase.BUILD,
            stage = progress.activeStage,
            wave = 1,
            height = foundation.size,
            tilt = GameConfig.START_TILT,
            mortar = GameConfig.START_MORTAR + stage.startMortarBonus,
            maxMortar = GameConfig.MAX_MORTAR,
            floors = foundation,
            trio = buildTrio(GameConfig.START_TILT, progress.unlockedRelics.size),
            selected = 0,
            keystoneCharges = stage.keystoneCharges,
            numericTilt = settings.numericTilt,
            detailedCards = settings.detailedCards
        )
        restartLoop()
    }

    fun selectCard(index: Int) {
        if (_state.value.phase != GamePhase.BUILD) return
        _state.update { it.copy(selected = index.coerceIn(0, (it.trio.size - 1).coerceAtLeast(0))) }
    }

    fun dropBlock() {
        val current = _state.value
        if (current.phase != GamePhase.BUILD || finishing) return
        val card = current.selectedCard ?: return
        placeCard(card)
    }

    fun bankKeystone() {
        val current = _state.value
        if (current.phase != GamePhase.BUILD || current.keystoneCharges <= 0) return
        val card = current.selectedCard ?: return
        _state.update {
            it.copy(keystoneBanked = card, keystoneCharges = it.keystoneCharges - 1)
        }
    }

    fun deployKeystone() {
        val current = _state.value
        if (current.phase != GamePhase.BUILD || finishing) return
        val card = current.keystoneBanked ?: return
        _state.update { it.copy(keystoneBanked = null) }
        placeCard(card)
    }

    fun reinforce(floorId: Int) {
        val current = _state.value
        if (!current.canReinforce) return
        val index = current.floors.indexOfFirst { it.id == floorId }
        if (index < 0) return
        val floor = current.floors[index]
        if (floor.integrity >= 100) return
        val patched = current.floors.toMutableList()
        patched[index] = floor.copy(
            integrity = (floor.integrity + GameConfig.REINFORCE_GAIN).coerceAtMost(100)
        )
        _state.update {
            it.copy(
                floors = patched,
                mortar = it.mortar - 1,
                reinforces = it.reinforces + 1,
                tilt = (it.tilt - GameConfig.REINFORCE_RELIEF).coerceAtLeast(0)
            )
        }
    }

    fun intercept(raiderId: Int) {
        val current = _state.value
        if (current.phase != GamePhase.WAVE) return
        if (current.raiders.none { it.id == raiderId }) return
        _state.update {
            it.copy(
                raiders = it.raiders.filterNot { raider -> raider.id == raiderId },
                mortar = (it.mortar + 1).coerceAtMost(it.maxMortar),
                intercepts = it.intercepts + 1,
                score = it.score + GameConfig.INTERCEPT_SCORE
            )
        }
    }

    fun retreat() {
        if (finishing) return
        finishRun(RunOutcome.RETREAT, withCollapse = false)
    }

    fun pause() {
        loop?.cancel()
        loop = null
    }

    private fun placeCard(card: BlockCard) {
        val current = _state.value
        val placement = applyBlock(
            floors = current.floors,
            card = card,
            tilt = current.tilt,
            maxTilt = GameConfig.MAX_TILT,
            nextFloorId = nextFloorId++,
            random = waveRandom
        )
        if (card.flaw == Flaw.TRUE_CUT) trueBlocks += 1
        familyDrops[card.family] = (familyDrops[card.family] ?: 0) + 1

        _state.update {
            it.copy(
                floors = placement.floors,
                tilt = placement.tilt,
                height = placement.floors.size
            )
        }
        phaseElapsed = 0L
        if (placement.tilt >= GameConfig.MAX_TILT) {
            finishRun(RunOutcome.TILT_COLLAPSE, withCollapse = true)
            return
        }
        startWave()
    }

    private fun startWave() {
        val current = _state.value
        val count = (2 + current.wave + stage.extraRaiders).coerceAtMost(GameConfig.MAX_RAIDERS)
        val crossMs = (GameConfig.RAIDER_BASE_CROSS_MS - current.wave * GameConfig.RAIDER_CROSS_STEP_MS)
            .coerceAtLeast(GameConfig.RAIDER_MIN_CROSS_MS)
        val floors = current.floors
        val raiders = List(count) { index ->
            val target = floors[(index * 2 + waveRandom.nextInt(floors.size)) % floors.size]
            Raider(
                id = nextRaiderId++,
                fromLeft = waveRandom.nextBoolean(),
                targetFloorId = target.id,
                lane = floors.indexOfFirst { it.id == target.id },
                progress = -0.18f * index,
                crossMs = crossMs
            )
        }
        _state.update {
            it.copy(
                phase = GamePhase.WAVE,
                raiders = raiders,
                raidersTotal = count,
                phaseFraction = 1f
            )
        }
    }

    private fun restartLoop() {
        loop?.cancel()
        loop = viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.TICK_MS)
                tick()
            }
        }
    }

    private fun tick() {
        if (finishing) return
        when (_state.value.phase) {
            GamePhase.BUILD -> tickBuild()
            GamePhase.WAVE -> tickWave()
            GamePhase.ENDED -> Unit
        }
        if (_state.value.bannerVisible) {
            bannerElapsed += GameConfig.TICK_MS
            if (bannerElapsed >= GameConfig.BANNER_MS) {
                bannerElapsed = 0L
                _state.update { it.copy(bannerVisible = false) }
            }
        }
        if (_state.value.shakeTicks > 0) {
            _state.update { it.copy(shakeTicks = it.shakeTicks - 1) }
        }
    }

    private fun tickBuild() {
        phaseElapsed += GameConfig.TICK_MS
        val fraction = 1f - (phaseElapsed.toFloat() / phaseBudget).coerceIn(0f, 1f)
        if (kotlin.math.abs(_state.value.phaseFraction - fraction) > 0.01f) {
            _state.update { it.copy(phaseFraction = fraction) }
        }
        if (phaseElapsed >= phaseBudget) {
            phaseElapsed = 0L
            dropBlock()
        }
    }

    private fun tickWave() {
        val current = _state.value
        if (current.raiders.isEmpty()) {
            clearWave()
            return
        }
        val advanced = mutableListOf<Raider>()
        val landed = mutableListOf<Raider>()
        current.raiders.forEach { raider ->
            val next = raider.progress + GameConfig.TICK_MS.toFloat() / raider.crossMs
            if (next >= 1f) landed.add(raider) else advanced.add(raider.copy(progress = next))
        }
        if (landed.isEmpty()) {
            _state.update { it.copy(raiders = advanced) }
            return
        }

        var floors = current.floors
        var tilt = current.tilt
        var lostFloor = false
        landed.forEach { raider ->
            val impact = resolveRaider(
                floors = floors,
                targetFloorId = raider.targetFloorId,
                wave = current.wave,
                armorMultiplier = stage.armorMultiplier,
                landedTilt = GameConfig.RAIDER_LANDED_TILT,
                floorLostTilt = GameConfig.FLOOR_LOST_TILT
            )
            floors = impact.floors
            tilt += impact.tiltAdded
            if (impact.floorLost) lostFloor = true
        }
        val clamped = tilt.coerceIn(0, GameConfig.MAX_TILT)
        _state.update {
            it.copy(
                raiders = advanced,
                floors = floors,
                tilt = clamped,
                height = floors.size,
                shakeTicks = if (lostFloor) 10 else it.shakeTicks
            )
        }
        if (clamped >= GameConfig.MAX_TILT) {
            finishRun(RunOutcome.TILT_COLLAPSE, withCollapse = true)
            return
        }
        if (floors.size < GameConfig.MIN_FLOORS) {
            finishRun(RunOutcome.HEIGHT_COLLAPSE, withCollapse = true)
            return
        }
        if (advanced.isEmpty()) clearWave()
    }

    private fun clearWave() {
        val current = _state.value
        val gained = GameConfig.WAVE_SCORE + current.height * GameConfig.WAVE_HEIGHT_SCORE
        val drift = stage.tiltDriftPerWave
        val nextWave = current.wave + 1
        waveRandom = seedFor(nextWave, activeRun)
        phaseElapsed = 0L
        bannerElapsed = 0L
        val relics = progressRepository.progress.value.unlockedRelics.size
        _state.update {
            it.copy(
                phase = GamePhase.BUILD,
                wave = nextWave,
                score = it.score + gained,
                tilt = (it.tilt + drift).coerceIn(0, GameConfig.MAX_TILT),
                raiders = emptyList(),
                raidersTotal = 0,
                bannerVisible = true,
                phaseFraction = 1f,
                trio = buildTrio(it.tilt + drift, relics),
                selected = 0
            )
        }
    }

    private fun buildTrio(tilt: Int, relicCount: Int): List<BlockCard> {
        val bias = BlockFamily.entries.associateWith { relicCount * GameConfig.RELIC_BIAS_PERCENT / 3 }
        val allBad = if (stage.id == StageId.DUSKLIGHT) 40 else 20
        return generateTrio(
            random = waveRandom,
            currentTilt = tilt,
            maxTilt = GameConfig.MAX_TILT,
            familyBias = bias,
            allBadPercent = allBad
        )
    }

    private fun finishRun(outcome: RunOutcome, withCollapse: Boolean) {
        if (finishing) return
        finishing = true
        val snapshot = _state.value
        _state.update { it.copy(phase = GamePhase.ENDED, collapsing = withCollapse, raiders = emptyList()) }
        viewModelScope.launch {
            if (withCollapse) delay(GameConfig.COLLAPSE_MS)
            pause()
            val tally = RunTally(
                outcome = outcome,
                stage = snapshot.stage,
                score = snapshot.score,
                waves = (snapshot.wave - 1).coerceAtLeast(0),
                height = snapshot.height,
                intercepts = snapshot.intercepts,
                reinforces = snapshot.reinforces,
                trueBlocks = trueBlocks,
                drops = familyDrops.toMap()
            )
            val evaluation = evaluateRun(tally, progressRepository.progress.value)
            progressRepository.update { _ -> evaluation.progress }
            _events.send(evaluation.result)
        }
    }

    private fun seedFor(wave: Int, runId: Int): Random {
        val stageIndex = StageId.entries.indexOf(stage.id) + 1
        return Random(stageIndex * 7919 xor (runId * 104729 + wave))
    }

    override fun onCleared() {
        pause()
        super.onCleared()
    }
}