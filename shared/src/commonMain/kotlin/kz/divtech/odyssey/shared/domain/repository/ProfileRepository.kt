package kz.divtech.odyssey.shared.domain.repository

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.DeviceInfo
import kz.divtech.odyssey.shared.domain.model.UpdatePhoneRequest
import kz.divtech.odyssey.shared.domain.model.auth.login.AuthRequest
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeResponse
import kz.divtech.odyssey.shared.domain.model.profile.Document
import kz.divtech.odyssey.shared.domain.model.profile.Profile
import kz.divtech.odyssey.shared.domain.model.profile.ProfileResponse

interface ProfileRepository {
    suspend fun getProfile(): Resource<ProfileResponse>

    suspend fun updateProfile(profile: Profile): Resource<HttpResponse>

    suspend fun sendDeviceInfo(deviceInfo: DeviceInfo): Resource<HttpResponse>

    suspend fun updateDocument(document: Document): Resource<HttpResponse>

    suspend fun updatePhoneNumberWihoutAuth(phoneRequest: UpdatePhoneRequest): Resource<HttpResponse>

    suspend fun updatePhoneNumberWithAuth(phoneNumber: String): Resource<CodeResponse>
    suspend fun updatePhoneConfirm(authRequest: AuthRequest) : Resource<HttpResponse>
    suspend fun insertProfile(profile: Profile)
    fun getProfileFromDb(): Flow<Profile?>
    suspend fun deleteProfile()
}