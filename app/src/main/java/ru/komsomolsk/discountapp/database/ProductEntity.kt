package ru.komsomolsk.discountapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность товара для хранения в Room.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
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
)
