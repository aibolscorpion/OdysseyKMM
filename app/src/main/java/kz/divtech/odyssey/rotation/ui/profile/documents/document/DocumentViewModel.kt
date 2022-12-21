package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Document
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class DocumentViewModel : ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    fun updateDocument(document: Document){
        pBarVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().updateDocument(document).enqueue(object: retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                pBarVisibility.set(View.GONE)
                if(response.isSuccessful){
                    Toast.makeText(App.appContext, R.string.application_sent, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                pBarVisibility.set(View.GONE)
            }

        })
    }
}