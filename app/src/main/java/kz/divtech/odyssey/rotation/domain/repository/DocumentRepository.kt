package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Document

class DocumentRepository(private val dao: Dao){

    val documents : Flow<List<Document>> = dao.observeDocuments()
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
        if(firstTime){
            val response = RetrofitClient.getApiService().getDocuments()
            if(response.isSuccess()){
                insertDocuments(response.asSuccess().value.documents)
                firstTime = false
            }
        }
    }
}