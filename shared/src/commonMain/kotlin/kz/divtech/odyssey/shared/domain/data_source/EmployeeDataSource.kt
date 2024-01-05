package kz.divtech.odyssey.shared.domain.data_source

import kz.divtech.odyssey.shared.domain.model.profile.Profile

interface EmployeeDataSource {
    suspend fun getProfile(): Profile?
    suspend fun insertProfile(profile: Profile)
    suspend fun deleteProfile()
}