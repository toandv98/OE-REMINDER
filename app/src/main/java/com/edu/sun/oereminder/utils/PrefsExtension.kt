package com.edu.sun.oereminder.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

fun Context.defaultPrefs(): SharedPreferences =
    PreferenceManager.getDefaultSharedPreferences(this)

fun Context.customPrefs(name: String): SharedPreferences =
    getSharedPreferences(name, Context.MODE_PRIVATE)

fun Context.encryptedPrefs(name: String): SharedPreferences {

    val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        this,
        name,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

operator fun SharedPreferences.set(key: String, value: Any?) = when (value) {
    is String? -> edit { it.putString(key, value) }
    is Int -> edit { it.putInt(key, value) }
    is Boolean -> edit { it.putBoolean(key, value) }
    is Float -> edit { it.putFloat(key, value) }
    is Long -> edit { it.putLong(key, value) }
    else -> throw UnsupportedOperationException("Not yet implemented")
}

inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T = when (T::class) {
    String::class -> getString(key, defaultValue as? String ?: "") as T
    Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
    Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
    Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
    Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
    else -> throw UnsupportedOperationException("Not yet implemented")
}
