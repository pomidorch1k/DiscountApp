package ru.komsomolsk.discountapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.komsomolsk.discountapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    private val filteredProducts: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)

        // Изначально показываем все товары из репозитория
        filteredProducts.clear()
        filteredProducts.addAll(ProductRepository.products)

        adapter = ProductAdapter(filteredProducts) { product ->
            onProductClicked(product)
        }
        binding.recyclerProducts.adapter = adapter

        setupSearch()
    }

    override fun onResume() {
        super.onResume()
        // Обновить список после возврата из админ‑панели
        applyFilter(binding.etSearch.text?.toString().orEmpty())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_admin -> {
                openAdminPanel()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                applyFilter(s?.toString().orEmpty())
            }
        })
    }

    private fun applyFilter(query: String) {
        val all = ProductRepository.products
        val filtered = if (query.isBlank()) {
            all
        } else {
            all.filter { it.name.contains(query, ignoreCase = true) }
        }
        filteredProducts.clear()
        filteredProducts.addAll(filtered)
        adapter.notifyDataSetChanged()
    }

    private fun onProductClicked(product: Product) {
        if (product.isOutOfStock) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.cannot_buy_title))
                .setMessage(getString(R.string.cannot_buy_message))
                .setPositiveButton(android.R.string.ok, null)
                .show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle(product.name)
            .setMessage(getString(R.string.buy_dialog_message, product.finalPrice))
            .setPositiveButton(R.string.buy) { _, _ ->
                handlePurchase(product)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun handlePurchase(product: Product) {
        val updated = product.copy(stockQuantity = product.stockQuantity - 1)
        ProductRepository.updateProduct(updated)
        applyFilter(binding.etSearch.text?.toString().orEmpty())
    }

    private fun openAdminPanel() {
        startActivity(AdminActivity.newIntent(this))
    }
}
