package ru.komsomolsk.discountapp

/**
 * Модель товара для отображения в карточке.
 */
data class Product(
    val id: Int,
    val category: String,
    val name: String,
    val description: String,
    val manufacturer: String,
    val supplier: String,
    val price: Double,
    val unit: String,
    val stockQuantity: Int,
    val discountPercent: Double,
    val imagePath: String? = null
) {
    /** Итоговая цена с учётом скидки */
    val finalPrice: Double
        get() = price * (1 - discountPercent / 100)

    /** Есть ли скидка (снижена ли цена) */
    val hasDiscount: Boolean
        get() = discountPercent > 0

    /** Товар отсутствует на складе */
    val isOutOfStock: Boolean
        get() = stockQuantity <= 0

    /** Скидка превышает 15% — для фона секции скидки */
    val hasHighDiscount: Boolean
        get() = discountPercent > 15
}
