package ru.komsomolsk.discountapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.komsomolsk.discountapp.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private var selectedProductId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SessionManager.currentRole != UserRole.ADMIN && SessionManager.currentRole != UserRole.MANAGER) {
            finish()
            return
        }
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAdmin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.roleManagementSection.visibility = if (SessionManager.currentRole == UserRole.ADMIN) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
        binding.btnSwapRoles.setOnClickListener {
            RoleRepository.swapRoles(this)
            Toast.makeText(this, R.string.role_swapped, Toast.LENGTH_SHORT).show()
        }

        loadProducts()
        setupButtons()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            val products = ProductRepository.getAll(this@AdminActivity)
            setupProductList(products)
        }
    }

    private fun setupProductList(products: List<Product>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            products.map { "${it.id}. ${it.name}" }
        )
        binding.listProducts.adapter = adapter

        binding.listProducts.setOnItemClickListener { _, _, position, _ ->
            val product = products[position]
            selectedProductId = product.id
            fillForm(product)
        }
    }

    private fun setupButtons() {
        binding.btnSave.setOnClickListener {
            val id = selectedProductId
            if (id == null) {
                Toast.makeText(this, R.string.select_product_first, Toast.LENGTH_SHORT).show()
            } else {
                val product = readProductFromForm(id = id) ?: return@setOnClickListener
                lifecycleScope.launch {
                    ProductRepository.updateProduct(this@AdminActivity, product)
                    Toast.makeText(this@AdminActivity, R.string.product_updated, Toast.LENGTH_SHORT).show()
                    loadProducts()
                }
            }
        }

        binding.btnAddNew.setOnClickListener {
            val product = readProductFromForm(id = 0) ?: return@setOnClickListener
            lifecycleScope.launch {
                ProductRepository.addProduct(this@AdminActivity, product)
                Toast.makeText(this@AdminActivity, R.string.product_added, Toast.LENGTH_SHORT).show()
                clearForm()
                loadProducts()
            }
        }
    }

    private fun fillForm(product: Product) {
        binding.etCategory.setText(product.category)
        binding.etName.setText(product.name)
        binding.etDescription.setText(product.description)
        binding.etManufacturer.setText(product.manufacturer)
        binding.etSupplier.setText(product.supplier)
        binding.etPrice.setText(product.price.toString())
        binding.etUnit.setText(product.unit)
        binding.etStock.setText(product.stockQuantity.toString())
        binding.etDiscount.setText(product.discountPercent.toString())
    }

    private fun clearForm() {
        selectedProductId = null
        binding.etCategory.text?.clear()
        binding.etName.text?.clear()
        binding.etDescription.text?.clear()
        binding.etManufacturer.text?.clear()
        binding.etSupplier.text?.clear()
        binding.etPrice.text?.clear()
        binding.etUnit.text?.clear()
        binding.etStock.text?.clear()
        binding.etDiscount.text?.clear()
    }

    private fun readProductFromForm(id: Int): Product? {
        val category = binding.etCategory.text?.toString()?.trim().orEmpty()
        val name = binding.etName.text?.toString()?.trim().orEmpty()
        val description = binding.etDescription.text?.toString()?.trim().orEmpty()
        val manufacturer = binding.etManufacturer.text?.toString()?.trim().orEmpty()
        val supplier = binding.etSupplier.text?.toString()?.trim().orEmpty()
        val priceText = binding.etPrice.text?.toString()?.trim().orEmpty()
        val unit = binding.etUnit.text?.toString()?.trim().orEmpty()
        val stockText = binding.etStock.text?.toString()?.trim().orEmpty()
        val discountText = binding.etDiscount.text?.toString()?.trim().orEmpty()

        if (name.isBlank() || category.isBlank()) {
            Toast.makeText(this, R.string.fill_required_fields, Toast.LENGTH_SHORT).show()
            return null
        }

        val price = priceText.toDoubleOrNull()
        val stock = stockText.toIntOrNull()
        val discount = discountText.toDoubleOrNull()

        if (price == null || stock == null || discount == null) {
            Toast.makeText(this, R.string.fill_numbers_correctly, Toast.LENGTH_SHORT).show()
            return null
        }

        return Product(
            id = id,
            category = category,
            name = name,
            description = description,
            manufacturer = manufacturer,
            supplier = supplier,
            price = price,
            unit = unit,
            stockQuantity = stock,
            discountPercent = discount
        )
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, AdminActivity::class.java)
    }
}
