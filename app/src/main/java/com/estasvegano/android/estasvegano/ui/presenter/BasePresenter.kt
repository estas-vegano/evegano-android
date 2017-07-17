package com.estasvegano.android.estasvegano.ui.presenter

import android.support.annotation.CallSuper
import com.estasvegano.android.estasvegano.ui.view.View
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

open class BasePresenter<V : View<VM>, VM>(initialState: VM) {

    val stateSubject: BehaviorSubject<VM> = BehaviorSubject.createDefault(initialState)

    val unbindViewDisposables: CompositeDisposable = CompositeDisposable()

    var view: V? = null

    @CallSuper
    open fun bindView(view: V) {
        if (this.view != null) {
            throw IllegalStateException("Can not bind new view = $view when old view = ${this.view} in bound")
        }

        this.view = view

        val disposable = stateSubject.subscribe({ view.bind(it) })
        unsubscribeOnUnbindView(disposable)
    }

    @CallSuper
    open fun unBindView(view: V) {
        if (view != this.view) {
            throw IllegalStateException("Old view = ${this.view} is not same as new view = $view")
        }

        this.view = null

        unbindViewDisposables.clear()
    }

    protected fun subscribeViewState(observable: Observable<VM>) {
        unsubscribeOnUnbindView(observable.delegateToSubject(stateSubject))
    }

    protected fun <T> safeWrap(viewObservable: Observable<T>): Observable<T> {
        val subject = PublishSubject.create<T>()
        unsubscribeOnUnbindView(viewObservable.delegateToSubject(subject))
        return subject
    }

    protected fun unsubscribeOnUnbindView(disposable: Disposable) {
        unbindViewDisposables.add(disposable)
    }
}

private fun <T> Observable<T>.delegateToSubject(subject: Subject<T>) =
        subscribe(
                subject::onNext,
                subject::onError,
                subject::onComplete,
                subject::onSubscribe
        )
