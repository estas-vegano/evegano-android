package com.estasvegano.android.estasvegano.ui.presenter

import com.estasvegano.android.estasvegano.entity.Category
import com.estasvegano.android.estasvegano.entity.Producer
import com.estasvegano.android.estasvegano.entity.Product
import com.estasvegano.android.estasvegano.entity.ProductType
import com.estasvegano.android.estasvegano.mock
import com.estasvegano.android.estasvegano.model.ProductModel
import com.estasvegano.android.estasvegano.ui.view.CodeReaderView
import com.estasvegano.android.estasvegano.ui.viewmodels.CodeInfo
import com.estasvegano.android.estasvegano.ui.viewmodels.CodeReaderViewModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(JUnitPlatform::class)
class CodeReaderPresenterTest : Spek({

    describe("Code reader presenter") {

        val testProduct = Product(
                1,
                "product",
                ProductType.VEGAN,
                null,
                Producer(1L, "Mallakto", true),
                Category(1L, "grocery"),
                listOf(Product.Code("code", "format"))
        )
        val testCode = CodeInfo("testCode", "testFormat")
        val eventsSubject = PublishSubject.create<CodeInfo>()
        val productModel = mock<ProductModel>()
        val view = mock<CodeReaderView>()
        val presenter = CodeReaderPresenter(
                CodeReaderViewModel.empty(),
                productModel,
                CodeReaderPresenter.Config(Schedulers.trampoline(), Schedulers.trampoline())
        )

        beforeEachTest {
            Mockito.clearInvocations(view)
        }

        on("events subject emits item") {

            presenter.bindView(view)
            eventsSubject.onNext(testCode)

            it("presenter should send loading view model") {
                Mockito.verify(view).bind(CodeReaderViewModel.loading())
            }

            on("product model returns product") {

                Mockito.`when`(productModel.checkCode(testCode.code, testCode.format)).thenReturn(Single.just(testProduct))
            }
        }
    }
})