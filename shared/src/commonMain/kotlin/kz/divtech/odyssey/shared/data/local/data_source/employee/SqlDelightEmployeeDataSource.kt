package kz.divtech.odyssey.shared.data.local.data_source.employee

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.EmployeeDataSource
import kz.divtech.odyssey.shared.domain.model.profile.Document
import kz.divtech.odyssey.shared.domain.model.profile.Profile

class SqlDelightEmployeeDataSource(dataBase: OdysseyDatabase): EmployeeDataSource {
    private val queries = dataBase.employeeQueries
    override fun getProfile(): Flow<Profile?> {
        return queries.getEmployee(mapper = { id, full_name, first_name, last_name, patronymic,
            first_name_en, last_name_en, birth_date, gender, country_code, iin, number,
            position, phone, additional_phone, email, ua_confirmed, documents ->
            val documents: List<Document>  = Json.decodeFromString(documents)
            Profile(
                id = id.toInt(),
                fullName = full_name,
                firstName = first_name,
                lastName = last_name,
                patronymic = patronymic,
                firstNameEn = first_name_en,
                lastNameEn = last_name_en,
                birthDate = birth_date,
                gender = gender,
                countryCode = country_code,
                iin = iin,
                number = number,
                position = position,
                phone = phone,
                additionalPhone = additional_phone,
                email = email,
                uaConfirmed = ua_confirmed == 1L,
                documents = documents
            )
        }).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    override fun insertProfile(profile: Profile) {
        val documents = Json.encodeToString(profile.documents)
        queries.insertEmployee(
            id = profile.id.toLong(),
            full_name = profile.fullName,
            first_name = profile.firstName,
            last_name = profile.lastName,
            patronymic = profile.patronymic,
            first_name_en = profile.firstNameEn,
            last_name_en = profile.lastNameEn,
            birth_date = profile.birthDate,
            gender = profile.gender,
            country_code = profile.countryCode,
            iin = profile.iin,
            number = profile.number,
            position = profile.position,
            phone = profile.phone,
            additional_phone = profile.additionalPhone,
            email = profile.email,
            ua_confirmed = if(profile.uaConfirmed) 1L else 0L,
            documents = documents
        )
    }

    override fun deleteProfile() {
        queries.deleteEmployee()
    }

    override fun getUAConfirmed(): Flow<Long?> {
        return queries.getUAConfirmed().asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    override fun updateUAConfirmed(uaConfirmed: Boolean) {
        queries.updateUAConfirmed(if(uaConfirmed) 1L else 0L)
    }
}