package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.profile.Profile

interface EmployeeDataSource {
    fun getProfile(): Flow<Profile?>
    suspend fun insertProfile(profile: Profile)
    suspend fun deleteProfile()
    fun getUAConfirmed(): Flow<Long?>
    suspend fun updateUAConfirmed(uaConfirmed: Boolean)
}