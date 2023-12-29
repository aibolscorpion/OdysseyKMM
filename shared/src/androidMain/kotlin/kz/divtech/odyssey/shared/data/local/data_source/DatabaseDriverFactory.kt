package kz.divtech.odyssey.shared.data.local.data_source

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import kz.divtech.odssey.database.OdysseyDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(OdysseyDatabase.Schema, context, "odyssey.db")
    }
}