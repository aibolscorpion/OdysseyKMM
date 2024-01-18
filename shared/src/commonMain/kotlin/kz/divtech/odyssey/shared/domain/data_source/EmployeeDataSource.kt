package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.profile.Profile

interface EmployeeDataSource {
    fun getProfile(): Flow<Profile?>
    fun insertProfile(profile: Profile)
    fun deleteProfile()
    fun getUAConfirmed(): Flow<Long?>
    fun updateUAConfirmed(uaConfirmed: Boolean)
}