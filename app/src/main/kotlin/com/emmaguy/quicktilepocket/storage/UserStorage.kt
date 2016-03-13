package com.emmaguy.quicktilepocket.storage

interface UserStorage {
    val unreadCount: Int
    fun storeUnreadCount(unreadCount: Int)

    val username: String
    fun storeUsername(username: String)

    val accessToken: String
    fun storeAccessToken(accessToken: String)

    val requestToken: String
    fun storeRequestToken(requestToken: String)
}
