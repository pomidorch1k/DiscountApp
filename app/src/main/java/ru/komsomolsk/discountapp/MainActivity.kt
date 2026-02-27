package ru.komsomolsk.discountapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.komsomolsk.discountapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Демонстрационные данные (обувная компания)
        val products = listOf(
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
        )

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerProducts.adapter = ProductAdapter(products)
    }
}
