package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.String

public class OrgInfoQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> getOrgInfo(mapper: (
    supportPhone: String,
    telegramId: String?,
    whatsappPhone: String,
  ) -> T): Query<T> = Query(-1_463_126_019, arrayOf("OrgInfo"), driver, "OrgInfo.sq", "getOrgInfo",
      "SELECT * FROM OrgInfo") { cursor ->
    mapper(
      cursor.getString(0)!!,
      cursor.getString(1),
      cursor.getString(2)!!
    )
  }

  public fun getOrgInfo(): Query<OrgInfo> = getOrgInfo { supportPhone, telegramId, whatsappPhone ->
    OrgInfo(
      supportPhone,
      telegramId,
      whatsappPhone
    )
  }

  public fun insertOrgInfo(
    supportPhone: String,
    telegramId: String?,
    whatsappPhone: String,
  ) {
    driver.execute(2_010_617_496,
        """INSERT OR REPLACE INTO OrgInfo(supportPhone, telegramId, whatsappPhone) VALUES (?,?,?)""",
        3) {
          bindString(0, supportPhone)
          bindString(1, telegramId)
          bindString(2, whatsappPhone)
        }
    notifyQueries(2_010_617_496) { emit ->
      emit("OrgInfo")
    }
  }

  public fun deleteOrgInfo() {
    driver.execute(-1_977_614_490, """DELETE FROM OrgInfo""", 0)
    notifyQueries(-1_977_614_490) { emit ->
      emit("OrgInfo")
    }
  }
}
