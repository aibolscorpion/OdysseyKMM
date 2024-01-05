package kz.divtech.odyssey.shared.data.local.data_source.employee

import database.Employee
import kotlinx.serialization.json.Json
import kz.divtech.odyssey.shared.domain.model.profile.Document
import kz.divtech.odyssey.shared.domain.model.profile.Profile

fun Employee.toProfile() : Profile {
    val documents: List<Document>  = Json.decodeFromString(documents)
    return Profile(
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
}