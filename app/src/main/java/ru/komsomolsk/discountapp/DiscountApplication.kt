package ru.komsomolsk.discountapp

import android.app.Application
import ru.komsomolsk.discountapp.database.AppDatabase

class DiscountApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}
