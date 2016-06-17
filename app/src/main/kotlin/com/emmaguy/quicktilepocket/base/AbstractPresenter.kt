package com.emmaguy.quicktilepocket.base

import rx.Subscription
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

abstract class AbstractPresenter<in V : AbstractPresenter.View> {
    private val subscriptions: CompositeSubscription = CompositeSubscription()
    private var view: View? = null

    open fun onAttachView(view: V) {
        Timber.d("onAttachView: " + view)
        if (this.view !== null) {
            throw IllegalStateException("View " + this.view + " has already been attached")
        }

        this.view = view
    }

    open fun onDetachView() {
        Timber.d("onDetachView: " + view)
        if (this.view == null) {
            throw IllegalStateException("View has already been detached")
        }

        this.view = null
        this.subscriptions.clear()
    }

    protected fun unsubscribeOnDetach(subscription: Subscription) {
        subscriptions.add(subscription)
    }

    interface View {
    }
}