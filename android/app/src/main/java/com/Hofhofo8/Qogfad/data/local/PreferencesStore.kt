package com.Hofhofo8.Qogfad.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun long(key: String, fallback: Long): Long = prefs.getLong(key, fallback)

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun text(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun write(block: SharedPreferences.Editor.() -> Unit) {
        val editor = prefs.edit()
        editor.block()
        editor.apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val STORE_NAME = "bastion_balance_store"
    }
}

object PrefKeys {
    const val BEST_SCORE = "best_score"
    const val BEST_HEIGHT = "best_height"
    const val TOTAL_WAVES = "total_waves"
    const val SHARDS = "shards"
    const val INTERCEPTS = "intercepts"
    const val REINFORCES = "reinforces"
    const val TRUE_BLOCKS = "true_blocks"
    const val FROSTFALL_RUNS = "frostfall_runs"
    const val BEST_WAVES_RUN = "best_waves_run"
    const val FAMILY_DROPS = "family_drops"
    const val RELICS = "relics"
    const val CHESTS = "chests"
    const val MISSION_CLAIMS = "mission_claims"
    const val ACTIVE_STAGE = "active_stage"

    const val NUMERIC_TILT = "numeric_tilt"
    const val DETAILED_CARDS = "detailed_cards"
    const val HIGH_CONTRAST = "high_contrast"
    const val REDUCED_MOTION = "reduced_motion"
    const val PACE_MS = "pace_ms"
}
