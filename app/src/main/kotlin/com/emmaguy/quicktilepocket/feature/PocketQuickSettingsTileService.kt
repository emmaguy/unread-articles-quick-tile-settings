package com.emmaguy.quicktilepocket.feature

import android.content.Intent
import android.os.IBinder
import android.service.quicksettings.TileService
import com.emmaguy.quicktilepocket.Inject
import com.emmaguy.quicktilepocket.Injector
import com.emmaguy.quicktilepocket.R
import timber.log.Timber
import java.util.*

class PocketQuickSettingsTileService : TileService(), Injector by Inject.instance {
    override fun onStartListening() {
        super.onStartListening()

        Timber.d("onStartListening, unread count: ${userStorage.unreadCount}")
        if (userStorage.unreadCount >= 0) {
            qsTile.label = String.format(Locale.getDefault(), getString(R.string.unread_articles_format),
                    userStorage.unreadCount)
        } else {
            qsTile.label = getString(R.string.unread_articles)
        }

        qsTile.updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()

        Timber.d("onStopListening")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("onBind")
        return super.onBind(intent)
    }

    override fun onTileAdded() {
        Timber.d("onTileAdded")
        super.onTileAdded()
    }

    override fun onTileRemoved() {
        Timber.d("onTileRemoved")
        super.onTileRemoved()
    }

    override fun onClick() {
        Timber.d("onClick")
        super.onClick()
    }
}
