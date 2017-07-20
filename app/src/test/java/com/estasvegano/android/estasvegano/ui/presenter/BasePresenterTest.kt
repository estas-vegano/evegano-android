package com.estasvegano.android.estasvegano.ui.presenter

import com.estasvegano.android.estasvegano.mock
import com.estasvegano.android.estasvegano.ui.view.View
import com.estasvegano.android.estasvegano.ui.viewmodels.ViewModel
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

@RunWith(JUnitPlatform::class)
class BasePresenterTest : Spek({

    describe("BasePresenter") {

        val initialState = TestViewModel()

        val view: View<TestViewModel> = mock()

        val presenter: BasePresenter<View<TestViewModel>, TestViewModel> = BasePresenter(initialState)

        afterEachTest { Mockito.clearInvocations(view) }

        group("bind view") {
            beforeEachTest {
                presenter.bindView(view)
            }

            it("view should receive initial value") {
                verify(view).bind(initialState)
            }

            on("second bind view") {

                it("should throw exception") {
                    Assertions.assertThatThrownBy { presenter.bindView(view) }
                            .isInstanceOf(IllegalStateException::class.java)
                            .hasMessage("Can not bind new view = $view when old view = $view in bound")
                }
            }
        }

        group("unbind view") {

            it("should throw exception if view not bound") {
                Assertions.assertThatThrownBy { presenter.unBindView(view) }
                        .isInstanceOf(IllegalStateException::class.java)
                        .hasMessage("Old view = null is not same as new view = $view")
            }

            group("when view bound") {

                beforeEachTest { presenter.bindView(view) }

                it("should throw exception if view to unbound is not equals to current") {
                    val newView = mock<View<TestViewModel>>()
                    Assertions.assertThatThrownBy { presenter.unBindView(newView) }
                            .isInstanceOf(IllegalStateException::class.java)
                            .hasMessage("Old view = $view is not same as new view = $newView")
                }
            }
        }

        group("safe wrap") {

            var testObservable: Subject<Int> = PublishSubject.create()

            on("called without view") {

                it("should throw exception") {
                    Assertions.assertThatThrownBy { presenter.safeWrap(testObservable) }
                            .isInstanceOf(IllegalStateException::class.java)
                            .hasMessage("Should be called after bindView")

                }
            }

            group("wrapping observable with view") {

                beforeEachTest {
                    presenter.bindView(view)
                    presenter.safeWrap(testObservable)
                }

                it("should subscribe it") {
                    testObservable.test().assertSubscribed()
                }

                on("unbind view") {
                    presenter.unBindView(view)

                    it("should unsubscribe from it") {
                        testObservable.test().assertNotSubscribed()
                    }
                }
            }
        }

        group("subscribe view state") {
            var testObservable: Subject<TestViewModel> = PublishSubject.create()

            val testModel = TestViewModel()

            on("called without view") {

                it("should throw exception") {
                    Assertions.assertThatThrownBy { presenter.subscribeViewState(testObservable) }
                            .isInstanceOf(IllegalStateException::class.java)
                            .hasMessage("Should be called after bindView")

                }
            }

            group("subscribing observable with view") {

                beforeEachTest {
                    presenter.bindView(view)
                    presenter.subscribeViewState(testObservable)
                }

                on("new data") {

                    testObservable.onNext(testModel)

                    it("should send data to view") {
                        verify(view).bind(testModel)
                    }
                }

                on("new data when view unbound") {

                    presenter.unBindView(view)
                    testObservable.onNext(testModel)

                    it("should not send data") {
                        verify(view, Mockito.never()).bind(testModel)
                    }
                }

                on("new data when after view rebound") {

                    presenter.unBindView(view)
                    testObservable.onNext(testModel)
                    presenter.bindView(view)

                    it("should send data") {
                        verify(view).bind(testModel)
                    }
                }
            }
        }
    }
})

private class TestViewModel : ViewModel