package id.ggdev.ggdevcommerce

import android.app.Application
import id.ggdev.ggdevcommerce.data.source.repository.AuthRepoInterface
import id.ggdev.ggdevcommerce.data.source.repository.ProductsRepoInterface

class GgDevCommerceApplication: Application() {
    val authRepository: AuthRepoInterface
        get() = ServiceLocator.provideAuthRepository(this)

    val productsRepository: ProductsRepoInterface
        get() = ServiceLocator.provideProductsRepository(this)

    override fun onCreate() {
        super.onCreate()
    }
}