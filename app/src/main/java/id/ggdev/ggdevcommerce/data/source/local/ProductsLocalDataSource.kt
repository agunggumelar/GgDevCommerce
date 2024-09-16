package id.ggdev.ggdevcommerce.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import id.ggdev.ggdevcommerce.data.Product
import id.ggdev.ggdevcommerce.data.Result
import id.ggdev.ggdevcommerce.data.Result.*
import id.ggdev.ggdevcommerce.data.source.ProductDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsLocalDataSource internal constructor(
	private val productsDao: ProductsDao,
	private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductDataSource {
	override fun observeProducts(): LiveData<Result<List<Product>>?> {
		return try {
			productsDao.observeProducts().map {
				Result.Success(it)
			}
		} catch (e: Exception) {
			MutableLiveData<Result<List<Product>>>(Result.Error(e))
		}
	}

	override fun observeProductsByOwner(ownerId: String): LiveData<Result<List<Product>>?> {
		return try {
			productsDao.observeProductsByOwner(ownerId).map {
				Result.Success(it)
			}
		} catch (e: Exception) {
			MutableLiveData<Result<List<Product>>>(Result.Error(e))
		}
	}

	override suspend fun getAllProducts(): Result<List<Product>> = withContext(ioDispatcher) {
		return@withContext try {
			Result.Success(productsDao.getAllProducts())
		} catch (e: Exception) {
			Error(e)
		}
	}

	override suspend fun getAllProductsByOwner(ownerId: String): Result<List<Product>> =
		withContext(ioDispatcher) {
			return@withContext try {
				Result.Success(productsDao.getProductsByOwnerId(ownerId))
			} catch (e: Exception) {
				Error(e)
			}
		}

	override suspend fun getProductById(productId: String): Result<Product> =
		withContext(ioDispatcher) {
			try {
				val product = productsDao.getProductById(productId)
				if (product != null) {
					return@withContext Result.Success(product)
				} else {
					return@withContext Error(Exception("Product Not Found!"))
				}
			} catch (e: Exception) {
				return@withContext Error(e)
			}
		}

	override suspend fun insertProduct(newProduct: Product) = withContext(ioDispatcher) {
		productsDao.insert(newProduct)
	}

	override suspend fun updateProduct(proData: Product) = withContext(ioDispatcher) {
		productsDao.insert(proData)
	}

	override suspend fun insertMultipleProducts(data: List<Product>) = withContext(ioDispatcher) {
		productsDao.insertListOfProducts(data)
	}

	override suspend fun deleteProduct(productId: String): Unit = withContext(ioDispatcher) {
		productsDao.deleteProductById(productId)
	}

	override suspend fun deleteAllProducts() = withContext(ioDispatcher) {
		productsDao.deleteAllProducts()
	}
}