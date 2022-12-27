package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Documents
import kz.divtech.odyssey.rotation.domain.repository.ApplicationsRepository
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class DocumentsViewModel(repository: ApplicationsRepository) : ViewModel() {

    private val _documents: MutableLiveData<Documents> = MutableLiveData()
    val documents : LiveData<Documents> = _documents

    val pBarVisibility = ObservableInt(View.GONE)

    fun getAllDocuments(){
        pBarVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getDocuments().enqueue(object: Callback,
            retrofit2.Callback<Documents> {
            override fun onResponse(call: Call<Documents>, response: Response<Documents>) {
                pBarVisibility.set(View.GONE)
                    if(response.isSuccessful){
                        _documents.postValue(response.body())
                    }
            }

            override fun onFailure(call: Call<Documents>, t: Throwable) {
                pBarVisibility.set(View.GONE)
            }
        })
    }

    val employee: LiveData<Employee> = repository.employee.asLiveData()

    class DocumentsViewModelFactory(val repository: ApplicationsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DocumentsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return DocumentsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }

}