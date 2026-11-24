package com.Hofhofo8.Qogfad.presentation.game

import com.Hofhofo8.Qogfad.domain.model.BlockCard
import com.Hofhofo8.Qogfad.domain.model.Floor
import com.Hofhofo8.Qogfad.domain.model.Raider
import com.Hofhofo8.Qogfad.domain.model.StageId

enum class GamePhase { BUILD, WAVE, ENDED }

data class GameUiState(
    val phase: GamePhase = GamePhase.BUILD,
    val stage: StageId = StageId.STONEWATCH,
    val wave: Int = 1,
    val height: Int = 3,
    val tilt: Int = 12,
    val mortar: Int = 3,
    val maxMortar: Int = 8,
    val score: Int = 0,
    val intercepts: Int = 0,
    val reinforces: Int = 0,
    val floors: List<Floor> = emptyList(),
    val trio: List<BlockCard> = emptyList(),
    val selected: Int = 0,
    val raiders: List<Raider> = emptyList(),
    val raidersTotal: Int = 0,
    val phaseFraction: Float = 1f,
    val bannerVisible: Boolean = false,
    val shakeTicks: Int = 0,
    val collapsing: Boolean = false,
    val keystoneBanked: BlockCard? = null,
    val keystoneCharges: Int = 1,
    val numericTilt: Boolean = false,
    val detailedCards: Boolean = true
) {
    val raidersLeft: Int get() = raiders.size
    val canReinforce: Boolean get() = phase == GamePhase.BUILD && mortar > 0
    val selectedCard: BlockCard? get() = trio.getOrNull(selected)
}
