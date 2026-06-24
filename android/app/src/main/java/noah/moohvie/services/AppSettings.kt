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

    private val selectedProviderIDsState = mutableStateOf(
        (prefs.getStringSet("selectedProviderIDs", emptySet()) ?: emptySet())
            .mapNotNull { it.toIntOrNull() }
            .toSet()
    )
    var selectedProviderIDs: Set<Int>
        get() = selectedProviderIDsState.value
        set(value) {
            selectedProviderIDsState.value = value
            prefs.edit().putStringSet("selectedProviderIDs", value.map { it.toString() }.toSet()).apply()
        }

    private val maxCertificationState = mutableStateOf(
        FrenchCertification.fromRawValue(prefs.getString("maxCertification", FrenchCertification.ALL.rawValue)!!)
    )
    var maxCertification: FrenchCertification
        get() = maxCertificationState.value
        set(value) {
            maxCertificationState.value = value
            prefs.edit().putString("maxCertification", value.rawValue).apply()
        }

    private val allowRewatchingState = mutableStateOf(prefs.getBoolean("allowRewatching", false))
    var allowRewatching: Boolean
        get() = allowRewatchingState.value
        set(value) {
            allowRewatchingState.value = value
            prefs.edit().putBoolean("allowRewatching", value).apply()
        }

    private val appLanguageState = mutableStateOf(
        AppLanguage.entries.firstOrNull { it.name == prefs.getString("appLanguage", AppLanguage.SYSTEM.name) }
            ?: AppLanguage.SYSTEM
    )
    var appLanguage: AppLanguage
        get() = appLanguageState.value
        set(value) {
            appLanguageState.value = value
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
