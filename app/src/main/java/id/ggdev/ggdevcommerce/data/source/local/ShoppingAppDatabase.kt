package id.ggdev.ggdevcommerce.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.ggdev.ggdevcommerce.data.Product
import id.ggdev.ggdevcommerce.data.UserData
import id.ggdev.ggdevcommerce.data.utils.DateTypeConvertors
import id.ggdev.ggdevcommerce.data.utils.ListTypeConverter
import id.ggdev.ggdevcommerce.data.utils.ObjectListTypeConvertor

@Database(entities = [UserData::class, Product::class], version = 2)
@TypeConverters(ListTypeConverter::class, ObjectListTypeConvertor::class, DateTypeConvertors::class)
abstract class ShoppingAppDatabase : RoomDatabase() {
	abstract fun userDao(): UserDao
	abstract fun productsDao(): ProductsDao

	companion object {
		@Volatile
		private var INSTANCE: ShoppingAppDatabase? = null

		fun getInstance(context: Context): ShoppingAppDatabase =
			INSTANCE ?: synchronized(this) {
				INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
			}

		private fun buildDatabase(context: Context) =
			Room.databaseBuilder(
				context.applicationContext,
				ShoppingAppDatabase::class.java, "ShoppingAppDb"
			)
				.fallbackToDestructiveMigration()
				.allowMainThreadQueries()
				.build()
	}
}