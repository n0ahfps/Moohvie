package noah.moohvie.services

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import java.io.File

class ProfileStore private constructor(private val context: Context) {
    private val prefs = context.getSharedPreferences("moohvie_settings", Context.MODE_PRIVATE)
    private val avatarFile = File(context.filesDir, "profile_avatar.jpg")

    private val usernameState = mutableStateOf(prefs.getString("profileUsername", "Cinéphile") ?: "Cinéphile")
    var username: String
        get() = usernameState.value
        set(value) {
            usernameState.value = value
            prefs.edit().putString("profileUsername", value).apply()
        }

    private val avatarPathState = mutableStateOf(if (avatarFile.exists()) avatarFile.absolutePath else null)
    val profileImagePath: String?
        get() = avatarPathState.value

    fun setProfileImage(uri: Uri) {
        context.contentResolver.openInputStream(uri)?.use { input ->
            avatarFile.outputStream().use { output -> input.copyTo(output) }
        }
        avatarPathState.value = avatarFile.absolutePath
    }

    companion object {
        @Volatile private var instance: ProfileStore? = null

        fun getInstance(context: Context): ProfileStore =
            instance ?: synchronized(this) {
                instance ?: ProfileStore(context.applicationContext).also { instance = it }
            }
    }
}
