package ru.komsomolsk.discountapp

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.komsomolsk.discountapp.database.ProductEntity

/**
 * Репозиторий товаров с использованием Room.
 */
object ProductRepository {

    private fun db(context: Context) =
        (context.applicationContext as DiscountApplication).database

    private fun ProductEntity.toProduct() = Product(
        id = id,
        category = category,
        name = name,
        description = description,
        manufacturer = manufacturer,
        supplier = supplier,
        price = price,
        unit = unit,
        stockQuantity = stockQuantity,
        discountPercent = discountPercent,
        imagePath = imagePath
    )

    private fun Product.toEntity() = ProductEntity(
        id = id,
        category = category,
        name = name,
        description = description,
        manufacturer = manufacturer,
        supplier = supplier,
        price = price,
        unit = unit,
        stockQuantity = stockQuantity,
        discountPercent = discountPercent,
        imagePath = imagePath
    )

    fun getAllFlow(context: Context): Flow<List<Product>> =
        db(context).productDao().getAllFlow().map { list -> list.map { it.toProduct() } }

    suspend fun getAll(context: Context): List<Product> =
        db(context).productDao().getAll().map { it.toProduct() }

    suspend fun addProduct(context: Context, product: Product) {
        val dao = db(context).productDao()
        val nextId = (dao.getMaxId() ?: 0) + 1
        dao.insert(product.copy(id = nextId).toEntity())
    }

    suspend fun updateProduct(context: Context, product: Product) {
        db(context).productDao().update(product.toEntity())
    }

    suspend fun updateStock(context: Context, productId: Int, newStock: Int) {
        val dao = db(context).productDao()
        dao.getById(productId)?.let { entity ->
            dao.update(entity.copy(stockQuantity = newStock))
        }
    }

    suspend fun seedIfEmpty(context: Context) {
        val dao = db(context).productDao()
        if (dao.getAll().isEmpty()) {
            listOf(
                Product(1, "Кроссовки", "Nike Air Max", "Спортивная обувь для бега",
                    "Nike", "ООО СпортТовары", 8990.0, "пара", 15, 20.0),
                Product(2, "Ботинки", "Timberland Premium", "Зимние ботинки",
                    "Timberland", "ИП ПоставкаОбуви", 15900.0, "пара", 3, 10.0),
                Product(3, "Туфли", "Ecco Classic", "Классические мужские туфли",
                    "Ecco", "ООО ОбувьРФ", 12900.0, "пара", 0, 25.0),
                Product(4, "Сандалии", "Adidas Comfort", "Летние сандалии",
                    "Adidas", "ООО СпортТовары", 4500.0, "пара", 42, 0.0),
                Product(5, "Сапоги", "Ugg Classic", "Тёплые зимние сапоги",
                    "UGG", "ИП ПоставкаОбуви", 18900.0, "пара", 7, 30.0)
            ).forEach { dao.insert(it.toEntity()) }
        }
    }
}
