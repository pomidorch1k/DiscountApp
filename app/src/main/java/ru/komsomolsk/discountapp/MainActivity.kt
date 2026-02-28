package ru.komsomolsk.discountapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.komsomolsk.discountapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = ProductAdapter(
            onProductClick = { product -> onProductClick(product) }
        )
        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerProducts.adapter = adapter

        lifecycleScope.launch {
            ProductRepository.seedIfEmpty(this@MainActivity)
        }

        lifecycleScope.launch {
            ProductRepository.getAllFlow(this@MainActivity).collect { products ->
                adapter.updateProducts(products)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.action_admin)?.isVisible = SessionManager.currentRole == UserRole.ADMIN
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_admin -> {
                if (SessionManager.currentRole == UserRole.ADMIN) {
                    startActivity(AdminActivity.newIntent(this))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onProductClick(product: Product) {
        if (product.isOutOfStock) {
            AlertDialog.Builder(this)
                .setTitle(R.string.cannot_buy_title)
                .setMessage(R.string.cannot_buy_message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.buy)
            .setMessage(getString(R.string.buy_dialog_message, product.finalPrice))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                lifecycleScope.launch {
                    ProductRepository.updateStock(
                        this@MainActivity,
                        product.id,
                        product.stockQuantity - 1
                    )
                    Toast.makeText(this@MainActivity, R.string.buy, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
