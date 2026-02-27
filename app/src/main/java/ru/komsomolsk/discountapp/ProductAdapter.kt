package ru.komsomolsk.discountapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.komsomolsk.discountapp.databinding.ItemProductCardBinding

class ProductAdapter(
    private val products: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    class ProductViewHolder(
        private val binding: ItemProductCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            // Категория | Наименование
            binding.tvCategoryName.text = "${product.category} | ${product.name}"
            binding.tvDescription.text = "${binding.root.context.getString(R.string.description)} ${product.description}"
            binding.tvManufacturer.text = "${binding.root.context.getString(R.string.manufacturer)} ${product.manufacturer}"
            binding.tvSupplier.text = "${binding.root.context.getString(R.string.supplier)} ${product.supplier}"
            binding.tvUnit.text = "${binding.root.context.getString(R.string.unit)} ${product.unit}"
            binding.tvStock.text = "${binding.root.context.getString(R.string.stock)} ${product.stockQuantity}"

            // Цена: если скидка — перечёркнутая красная + итоговая чёрная
            if (product.hasDiscount) {
                binding.tvPrice.apply {
                    text = "${product.price} руб."
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                    visibility = View.VISIBLE
                }
                binding.tvFinalPrice.apply {
                    text = "${product.finalPrice} руб."
                    visibility = View.VISIBLE
                }
            } else {
                binding.tvPrice.apply {
                    text = "${product.price} руб."
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    visibility = View.VISIBLE
                }
                binding.tvFinalPrice.visibility = View.GONE
            }

            // Действующая скидка: фон #2E8B57 при скидке > 15%, иначе серый
            binding.tvDiscount.text = "${binding.root.context.getString(R.string.current_discount)}\n${product.discountPercent.toInt()}%"
            if (product.hasHighDiscount) {
                binding.layoutDiscount.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.discount_highlight)
                )
                binding.tvDiscount.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
            } else {
                binding.layoutDiscount.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.darker_gray)
                )
                binding.tvDiscount.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
            }

            // Фото: показываем заглушку (в реальном приложении — загрузка из imagePath)
            binding.tvPhotoPlaceholder.visibility = View.VISIBLE
            binding.ivProductPhoto.setImageResource(0)
            binding.ivProductPhoto.visibility = if (product.imagePath != null) View.VISIBLE else View.GONE

            // Товара нет на складе — выделить строку голубым
            val contentColor = if (product.isOutOfStock) {
                ContextCompat.getColor(binding.root.context, R.color.out_of_stock)
            } else {
                ContextCompat.getColor(binding.root.context, android.R.color.white)
            }
            binding.layoutCardContent.setBackgroundColor(contentColor)
        }
    }
}
