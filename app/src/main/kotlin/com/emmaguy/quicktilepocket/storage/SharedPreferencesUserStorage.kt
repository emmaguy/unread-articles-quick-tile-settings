package com.emmaguy.quicktilepocket.storage

import android.content.SharedPreferences
import android.content.res.Resources
import com.emmaguy.quicktilepocket.R

class SharedPreferencesUserStorage(private val sharedPreferences: SharedPreferences,
                                   private val resources: Resources) : UserStorage {
    override val unreadCount = sharedPreferences.getInt(resources.getString(R.string.pref_key_unread_count), -1)
    override val username = sharedPreferences.getString(resources.getString(R.string.pref_key_username), "")
    override val accessToken = sharedPreferences.getString(resources.getString(R.string.pref_key_access_token), "")
    override val requestToken = sharedPreferences.getString(resources.getString(R.string.pref_key_request_token), "")

    override fun storeUnreadCount(unreadCount: Int) {
        sharedPreferences.edit().putInt(resources.getString(R.string.pref_key_unread_count), unreadCount).apply()
    }

    override fun storeUsername(username: String) {
        sharedPreferences.edit().putString(resources.getString(R.string.pref_key_username), username).apply()
    }

    override fun storeAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(resources.getString(R.string.pref_key_access_token), accessToken).apply()
    }

    override fun storeRequestToken(requestToken: String) {
        sharedPreferences.edit().putString(resources.getString(R.string.pref_key_request_token), requestToken).apply()
    }
}
