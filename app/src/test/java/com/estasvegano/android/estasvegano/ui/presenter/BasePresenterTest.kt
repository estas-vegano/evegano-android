package com.estasvegano.android.estasvegano.ui.presenter

import com.estasvegano.android.estasvegano.mock
import com.estasvegano.android.estasvegano.ui.view.View
import com.estasvegano.android.estasvegano.ui.viewmodels.ViewModel
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@RunWith(JUnitPlatform::class)
class BasePresenterTest : Spek({

    describe("BasePresenter") {

        val initialState = TestViewModel()

        val view: View<TestViewModel> = mock()

        val presenter: BasePresenter<View<TestViewModel>, TestViewModel> = BasePresenter(initialState)

        on("bind view") {
            presenter.bindView(view)

            it("view should receive initial value") {
                verify(view).bind(initialState)
            }
        }
    }
})

private class TestViewModel : ViewModel