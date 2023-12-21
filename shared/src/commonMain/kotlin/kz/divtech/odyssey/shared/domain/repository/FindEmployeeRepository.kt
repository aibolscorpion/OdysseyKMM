package kz.divtech.odyssey.shared.domain.repository

import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.auth.search_employee.EmployeeResult

interface FindEmployeeRepository {
    suspend fun findByPhoneNumber(phoneNumber: String): Resource<EmployeeResult>
    suspend fun findByIIN(iin: String): Resource<EmployeeResult>
}