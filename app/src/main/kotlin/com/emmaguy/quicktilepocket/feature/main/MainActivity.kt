package com.emmaguy.quicktilepocket.feature.main

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.emmaguy.quicktilepocket.Inject
import com.emmaguy.quicktilepocket.Injector
import com.emmaguy.quicktilepocket.R
import com.emmaguy.quicktilepocket.base.AbstractActivity
import com.emmaguy.quicktilepocket.base.AbstractPresenter
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxrelay.PublishRelay
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable

class MainActivity() : AbstractActivity<MainPresenter.View>(), MainPresenter.View, Injector by Inject.instance {
    private val returnFromBrowserRelay: PublishRelay<Void> = PublishRelay.create()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getPresenter(): AbstractPresenter<MainPresenter.View> {
        return mainPresenter
    }

    override fun getPresenterView(): MainPresenter.View {
        return this
    }

    override fun onResume() {
        super.onResume()

        val uri = intent.data
        if (uri != null && uri.toString().startsWith(callbackUrlScheme)) {
            intent.data = null
            returnFromBrowserRelay.call(null)
        }
    }

    override fun onBeginLogin(): Observable<Void> {
        return RxView.clicks(loginButton)
    }

    override fun onReturnFromBrowser(): Observable<Void> {
        return returnFromBrowserRelay
    }

    override fun startBrowserForLogin(url: String) {
        Toast.makeText(this, R.string.redirecting_to_browser, Toast.LENGTH_SHORT).show()

        finish()
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun showLoggedIn() {
        loginButton.visibility = View.GONE
        infoTextView.text = res.getString(R.string.info_logged_in)
    }
}
