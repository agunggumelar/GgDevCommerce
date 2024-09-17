package id.ggdev.ggdevcommerce

import android.content.Context
import androidx.annotation.VisibleForTesting
import id.ggdev.ggdevcommerce.data.AppSessionManager
import id.ggdev.ggdevcommerce.data.source.ProductDataSource
import id.ggdev.ggdevcommerce.data.source.UserDataSource
import id.ggdev.ggdevcommerce.data.source.local.ProductsLocalDataSource
import id.ggdev.ggdevcommerce.data.source.local.ShoppingAppDatabase
import id.ggdev.ggdevcommerce.data.source.local.UserLocalDataSource
import id.ggdev.ggdevcommerce.data.source.remote.AuthRemoteDataSource
import id.ggdev.ggdevcommerce.data.source.remote.ProductsRemoteDataSource
import id.ggdev.ggdevcommerce.data.source.repository.AuthRepoInterface
import id.ggdev.ggdevcommerce.data.source.repository.AuthRepository
import id.ggdev.ggdevcommerce.data.source.repository.ProductsRepoInterface
import id.ggdev.ggdevcommerce.data.source.repository.ProductsRepository

object ServiceLocator {
	private var database: ShoppingAppDatabase? = null
	private val lock = Any()

	@Volatile
	var authRepository: AuthRepoInterface? = null
		@VisibleForTesting set

	@Volatile
	var productsRepository: ProductsRepoInterface? = null
		@VisibleForTesting set

	fun provideAuthRepository(context: Context): AuthRepoInterface {
		synchronized(this) {
			return authRepository ?: createAuthRepository(context)
		}
	}

	fun provideProductsRepository(context: Context): ProductsRepoInterface {
		synchronized(this) {
			return productsRepository ?: createProductsRepository(context)
		}
	}

	@VisibleForTesting
	fun resetRepository() {
		synchronized(lock) {
			database?.apply {
				clearAllTables()
				close()
			}
			database = null
			authRepository = null
		}
	}

	private fun createProductsRepository(context: Context): ProductsRepoInterface {
		val newRepo =
			ProductsRepository(ProductsRemoteDataSource(), createProductsLocalDataSource(context))
		productsRepository = newRepo
		return newRepo
	}

	private fun createAuthRepository(context: Context): AuthRepoInterface {
		val appSession = AppSessionManager(context.applicationContext)
		val newRepo =
			AuthRepository(createUserLocalDataSource(context), AuthRemoteDataSource(), appSession)
		authRepository = newRepo
		return newRepo
	}

	private fun createProductsLocalDataSource(context: Context): ProductDataSource {
		val database = database ?: ShoppingAppDatabase.getInstance(context.applicationContext)
		return ProductsLocalDataSource(database.productsDao())
	}

	private fun createUserLocalDataSource(context: Context): UserDataSource {
		val database = database ?: ShoppingAppDatabase.getInstance(context.applicationContext)
		return UserLocalDataSource(database.userDao())
	}
}