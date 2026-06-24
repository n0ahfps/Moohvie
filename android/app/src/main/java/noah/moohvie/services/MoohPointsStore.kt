package noah.moohvie.services

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class MoohPointsStore private constructor(context: Context) {
    private val prefs = context.getSharedPreferences("moohvie_settings", Context.MODE_PRIVATE)
    private val storageKey = "moohPointsTotal"

    var totalPoints: Int by mutableIntStateOf(prefs.getInt(storageKey, 0))
        private set

    fun award(points: Int) {
        totalPoints += points
        persist()
    }

    fun refund(points: Int) {
        totalPoints = maxOf(0, totalPoints - points)
        persist()
    }

    fun spend(points: Int): Boolean {
        if (totalPoints < points) return false
        totalPoints -= points
        persist()
        return true
    }

    private fun persist() {
        prefs.edit().putInt(storageKey, totalPoints).apply()
    }

    companion object {
        @Volatile private var instance: MoohPointsStore? = null

        fun getInstance(context: Context): MoohPointsStore =
            instance ?: synchronized(this) {
                instance ?: MoohPointsStore(context.applicationContext).also { instance = it }
            }
    }
}
