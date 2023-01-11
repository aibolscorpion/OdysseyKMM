package kz.divtech.odyssey.rotation.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.trips.Data

@androidx.room.Dao
interface Dao {
    //Trips
    @Query("SELECT * FROM data")
    fun getTrips(): Flow<Data>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(data: Data)

    @Query("DELETE FROM data")
    suspend fun deleteTrips()

    //Employee
    @Query("SELECT * FROM employee")
    fun getEmployee(): Flow<Employee>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)

    @Query("DELETE FROM employee")
    suspend fun deleteEmployee()

    //FAQ
    @Query("SELECT * FROM faq")
    fun getFAQ(): Flow<List<Faq>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFAQ(faqList: List<Faq>)

    @Query("DELETE FROM faq")
    suspend fun deleteFAQ()

}