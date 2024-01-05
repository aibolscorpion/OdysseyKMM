package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class EmployeeQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getEmployee(mapper: (
    id: Long,
    full_name: String,
    first_name: String,
    last_name: String,
    patronymic: String?,
    first_name_en: String?,
    last_name_en: String?,
    birth_date: String,
    gender: String,
    country_code: String,
    iin: String,
    number: String,
    position: String,
    phone: String?,
    additional_phone: String?,
    email: String?,
    ua_confirmed: Long,
    documents: String,
  ) -> T): Query<T> = Query(-564_358_359, arrayOf("Employee"), driver, "Employee.sq", "getEmployee",
      "SELECT * FROM Employee") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7)!!,
      cursor.getString(8)!!,
      cursor.getString(9)!!,
      cursor.getString(10)!!,
      cursor.getString(11)!!,
      cursor.getString(12)!!,
      cursor.getString(13),
      cursor.getString(14),
      cursor.getString(15),
      cursor.getLong(16)!!,
      cursor.getString(17)!!
    )
  }

  public fun getEmployee(): Query<Employee> = getEmployee { id, full_name, first_name, last_name,
      patronymic, first_name_en, last_name_en, birth_date, gender, country_code, iin, number,
      position, phone, additional_phone, email, ua_confirmed, documents ->
    Employee(
      id,
      full_name,
      first_name,
      last_name,
      patronymic,
      first_name_en,
      last_name_en,
      birth_date,
      gender,
      country_code,
      iin,
      number,
      position,
      phone,
      additional_phone,
      email,
      ua_confirmed,
      documents
    )
  }

  public fun insertEmployee(
    id: Long?,
    full_name: String,
    first_name: String,
    last_name: String,
    patronymic: String?,
    first_name_en: String?,
    last_name_en: String?,
    birth_date: String,
    gender: String,
    country_code: String,
    iin: String,
    number: String,
    position: String,
    phone: String?,
    additional_phone: String?,
    email: String?,
    ua_confirmed: Long,
    documents: String,
  ) {
    driver.execute(-55_002_270, """
        |INSERT OR REPLACE INTO Employee(
        |id, full_name, first_name, last_name, patronymic, first_name_en, last_name_en, birth_date, gender,
        |country_code, iin, number, position, phone, additional_phone, email, ua_confirmed, documents) VALUES (
        |?, ?, ?, ?,?, ?, ?, ?,
        |?, ?, ?, ?,?, ?, ?, ?, ?, ?)
        """.trimMargin(), 18) {
          bindLong(0, id)
          bindString(1, full_name)
          bindString(2, first_name)
          bindString(3, last_name)
          bindString(4, patronymic)
          bindString(5, first_name_en)
          bindString(6, last_name_en)
          bindString(7, birth_date)
          bindString(8, gender)
          bindString(9, country_code)
          bindString(10, iin)
          bindString(11, number)
          bindString(12, position)
          bindString(13, phone)
          bindString(14, additional_phone)
          bindString(15, email)
          bindLong(16, ua_confirmed)
          bindString(17, documents)
        }
    notifyQueries(-55_002_270) { emit ->
      emit("Employee")
    }
  }

  public fun deleteEmployee() {
    driver.execute(863_857_748, """DELETE FROM Employee""", 0)
    notifyQueries(863_857_748) { emit ->
      emit("Employee")
    }
  }
}
