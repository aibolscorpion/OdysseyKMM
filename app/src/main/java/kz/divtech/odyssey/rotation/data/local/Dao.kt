package kz.divtech.odyssey.rotation.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.trips.Data

@androidx.room.Dao
interface Dao {

    @Query("SELECT * FROM data")
    fun getAllApplications(): Flow<Data>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllApplications(vararg data: Data)

    @Delete
    suspend fun deleteAllApplications(data: Data)

    @Query("SELECT * FROM employee")
    fun getEmployeeInfo(): Flow<Employee>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmployeeInfo(vararg employee: Employee)

    @Delete
    suspend fun deleteEmployeeInfo(employee: Employee)

}