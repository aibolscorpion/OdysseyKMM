package kz.divtech.odyssey.rotation.domain.model.login.login.employee_response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.lang.reflect.Type

@Parcelize
@TypeConverters(DocumentListConverter::class)
@Entity
data class Employee(
    @PrimaryKey val id: Int,
    val full_name: String,
    var first_name: String,
    var last_name: String,
    var patronymic: String?,
    var first_name_en: String?,
    var last_name_en: String?,
    var birth_date: String,
    var gender: String,
    var country_code: String,
    var iin: String,
    val number: String,
    val position: String,
    var phone: String?,
    val additional_phone: String?,
    var email: String?,
    val ua_confirmed: Boolean,
    val documents: @RawValue List<Document>
): Parcelable

class DocumentListConverter {
    @TypeConverter
    fun fromString(value: String?): List<Document> {
        val listType: Type = object : TypeToken<List<Document>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromData(data: List<Document>): String {
        val gson = Gson()
        return gson.toJson(data)
    }
}