package com.emmaguy.quicktilepocket.feature

import android.service.quicksettings.TileService
import com.emmaguy.quicktilepocket.Inject
import com.emmaguy.quicktilepocket.Injector
import com.emmaguy.quicktilepocket.R
import java.util.*

class PocketQuickSettingsTileService : TileService(), Injector by Inject.instance {
    override fun onStartListening() {
        super.onStartListening()

        if (userStorage.unreadCount >= 0) {
            qsTile.label = String.format(Locale.getDefault(), getString(R.string.unread_articles_format),
                    userStorage.unreadCount)
        } else {
            qsTile.label = getString(R.string.unread_articles)
        }

        qsTile.updateTile()
    }
}
