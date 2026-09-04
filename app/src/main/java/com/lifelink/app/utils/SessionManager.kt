package com.lifelink.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SessionManager {

    private const val PREF_NAME = "LifeLinkSecureSession"
    private const val KEY_DONOR_ID    = "donor_id"
    private const val KEY_FIRST_NAME  = "first_name"
    private const val KEY_BLOOD_TYPE  = "blood_type"
    private const val KEY_IS_LOGGED   = "is_logged_in"

    // Fallback to standard SharedPreferences if EncryptedSharedPreferences fails (common on some devices/emulators)
    private fun getPrefs(context: Context): SharedPreferences {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            EncryptedSharedPreferences.create(
                context,
                PREF_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun saveSession(context: Context, donorId: Int, firstName: String, bloodType: String) {
        getPrefs(context).edit()
            .putInt(KEY_DONOR_ID, donorId)
            .putString(KEY_FIRST_NAME, firstName)
            .putString(KEY_BLOOD_TYPE, bloodType)
            .putBoolean(KEY_IS_LOGGED, true)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_IS_LOGGED, false)

    fun getDonorId(context: Context): Int =
        getPrefs(context).getInt(KEY_DONOR_ID, -1)

    fun getFirstName(context: Context): String =
        getPrefs(context).getString(KEY_FIRST_NAME, "") ?: ""

    fun getBloodType(context: Context): String =
        getPrefs(context).getString(KEY_BLOOD_TYPE, "") ?: ""

    fun clearSession(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
