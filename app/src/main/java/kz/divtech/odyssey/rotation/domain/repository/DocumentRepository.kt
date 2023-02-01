package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Document
import timber.log.Timber

class DocumentRepository(private val dao: Dao){

    val documents : Flow<List<Document>> = dao.getDocuments()
    private var firstTime = true

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun insertDocuments(documents : List<Document>){
        dao.insertDocuments(documents)
    }

    @WorkerThread
    @Suppress("RedundantSuspendModifier")
    suspend fun deleteDocuments() {
        dao.deleteDocuments()
    }

    suspend fun getDocumentsFromServer(){
        try{
            if(firstTime){
                firstTime = false
                val response = RetrofitClient.getApiService().getDocuments()
                if(response.isSuccessful){
                    insertDocuments(response.body()!!.documents)
                }
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }
}