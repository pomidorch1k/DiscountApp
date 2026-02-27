package ru.komsomolsk.discountapp

/**
 * Простое хранилище товаров в памяти.
 * Используется основной экран и админ‑панелью.
 */
object ProductRepository {

    /** Полный список товаров в приложении. */
    val products: MutableList<Product> = mutableListOf(
        Product(
            id = 1,
            category = "Кроссовки",
            name = "Nike Air Max",
            description = "Спортивная обувь для бега",
            manufacturer = "Nike",
            supplier = "ООО СпортТовары",
            price = 8990.0,
            unit = "пара",
            stockQuantity = 15,
            discountPercent = 20.0
        ),
        Product(
            id = 2,
            category = "Ботинки",
            name = "Timberland Premium",
            description = "Зимние ботинки",
            manufacturer = "Timberland",
            supplier = "ИП ПоставкаОбуви",
            price = 15900.0,
            unit = "пара",
            stockQuantity = 3,
            discountPercent = 10.0
        ),
        Product(
            id = 3,
            category = "Туфли",
            name = "Ecco Classic",
            description = "Классические мужские туфли",
            manufacturer = "Ecco",
            supplier = "ООО ОбувьРФ",
            price = 12900.0,
            unit = "пара",
            stockQuantity = 0,
            discountPercent = 25.0
        ),
        Product(
            id = 4,
            category = "Сандалии",
            name = "Adidas Comfort",
            description = "Летние сандалии",
            manufacturer = "Adidas",
            supplier = "ООО СпортТовары",
            price = 4500.0,
            unit = "пара",
            stockQuantity = 42,
            discountPercent = 0.0
        ),
        Product(
            id = 5,
            category = "Сапоги",
            name = "Ugg Classic",
            description = "Тёплые зимние сапоги",
            manufacturer = "UGG",
            supplier = "ИП ПоставкаОбуви",
            price = 18900.0,
            unit = "пара",
            stockQuantity = 7,
            discountPercent = 30.0
        )
    )

    /** Возвращает максимальный текущий id. */
    private fun maxId(): Int = products.maxOfOrNull { it.id } ?: 0

    /** Добавление нового товара. */
    fun addProduct(product: Product) {
        val nextId = maxId() + 1
        products.add(product.copy(id = nextId))
    }

    /** Обновление существующего товара по id. */
    fun updateProduct(updated: Product) {
        val index = products.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            products[index] = updated
        }
    }
}

