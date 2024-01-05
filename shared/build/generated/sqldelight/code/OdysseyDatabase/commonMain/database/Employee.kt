package database

import kotlin.Long
import kotlin.String

public data class Employee(
  public val id: Long,
  public val full_name: String,
  public val first_name: String,
  public val last_name: String,
  public val patronymic: String?,
  public val first_name_en: String?,
  public val last_name_en: String?,
  public val birth_date: String,
  public val gender: String,
  public val country_code: String,
  public val iin: String,
  public val number: String,
  public val position: String,
  public val phone: String?,
  public val additional_phone: String?,
  public val email: String?,
  public val ua_confirmed: Long,
  public val documents: String,
)
