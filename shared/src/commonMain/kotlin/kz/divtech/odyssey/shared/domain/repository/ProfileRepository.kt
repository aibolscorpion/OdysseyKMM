package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.DeviceInfo
import kz.divtech.odyssey.shared.domain.model.UpdatePhoneRequest
import kz.divtech.odyssey.shared.domain.model.employee.Profile
import kz.divtech.odyssey.shared.domain.model.employee.ProfileResponse

interface ProfileRepository {
    suspend fun getProfile(): Resource<ProfileResponse>

    suspend fun updateProfile(profile: Profile): Resource<HttpResponse>

    suspend fun sendDeviceInfo(deviceInfo: DeviceInfo): Resource<HttpResponse>

    suspend fun updatePhoneNumberWihoutAuth(phoneRequest: UpdatePhoneRequest): Resource<HttpResponse>
}