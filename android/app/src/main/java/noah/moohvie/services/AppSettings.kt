package noah.moohvie.services

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import noah.moohvie.models.AppLanguage
import noah.moohvie.models.FrenchCertification

class AppSettings private constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("moohvie_settings", Context.MODE_PRIVATE)

    var selectedProviderIDs: Set<Int> by mutableStateOf(
        (prefs.getStringSet("selectedProviderIDs", emptySet()) ?: emptySet())
            .mapNotNull { it.toIntOrNull() }
            .toSet()
    )
        set(value) {
            field = value
            prefs.edit().putStringSet("selectedProviderIDs", value.map { it.toString() }.toSet()).apply()
        }

    var maxCertification: FrenchCertification by mutableStateOf(
        FrenchCertification.fromRawValue(prefs.getString("maxCertification", FrenchCertification.ALL.rawValue)!!)
    )
        set(value) {
            field = value
            prefs.edit().putString("maxCertification", value.rawValue).apply()
        }

    var allowRewatching: Boolean by mutableStateOf(prefs.getBoolean("allowRewatching", false))
        set(value) {
            field = value
            prefs.edit().putBoolean("allowRewatching", value).apply()
        }

    var appLanguage: AppLanguage by mutableStateOf(
        AppLanguage.entries.firstOrNull { it.name == prefs.getString("appLanguage", AppLanguage.SYSTEM.name) }
            ?: AppLanguage.SYSTEM
    )
        set(value) {
            field = value
            prefs.edit().putString("appLanguage", value.name).apply()
        }

    companion object {
        @Volatile private var instance: AppSettings? = null

        fun getInstance(context: Context): AppSettings =
            instance ?: synchronized(this) {
                instance ?: AppSettings(context.applicationContext).also { instance = it }
            }
    }
}
