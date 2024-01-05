package kz.divtech.odyssey.shared.data.local.data_source.employee

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kz.divtech.odssey.database.OdysseyDatabase
import kz.divtech.odyssey.shared.domain.data_source.EmployeeDataSource
import kz.divtech.odyssey.shared.domain.model.profile.Profile

class SqlDelightEmployeeDataSource(dataBase: OdysseyDatabase): EmployeeDataSource {
    private val queries = dataBase.employeeQueries
    override suspend fun getProfile(): Profile? {
        return queries.getEmployee().executeAsOneOrNull()?.toProfile()
    }

    override suspend fun insertProfile(profile: Profile) {
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

    override suspend fun deleteProfile() {
        queries.deleteEmployee()
    }
}