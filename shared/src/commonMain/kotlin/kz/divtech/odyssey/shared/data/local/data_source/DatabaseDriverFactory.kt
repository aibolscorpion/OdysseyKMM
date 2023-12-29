package kz.divtech.odyssey.shared.data.local.data_source

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}