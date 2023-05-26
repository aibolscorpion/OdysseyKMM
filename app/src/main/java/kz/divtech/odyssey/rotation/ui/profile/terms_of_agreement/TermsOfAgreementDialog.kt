package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.LoginNavGraphDirections
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogTermsOfAgreementBinding
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.MainActivityDirections
import kz.divtech.odyssey.rotation.ui.login.LoginActivity
import kz.divtech.odyssey.rotation.utils.NetworkUtils.isNetworkAvailable


class TermsOfAgreementDialog : BottomSheetDialogFragment() {

    val viewModel: TermsOfAgreementViewModel by viewModels()
    private var _dataBinding: DialogTermsOfAgreementBinding? = null
    val dataBinding get() = _dataBinding!!

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataBinding  = DialogTermsOfAgreementBinding.inflate(inflater)
        dataBinding.thisDialog = this
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                dataBinding.termsProgressBar.visibility = View.GONE
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?,
                                         error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                dataBinding.termsProgressBar.visibility = View.GONE
            }
        }

        viewModel.htmlLiveData.observe(viewLifecycleOwner){ htmlText ->
            showData(htmlText)
        }

        if(viewModel.getFile().exists()){
            val htmlText = viewModel.getFile().readText()
            showData(htmlText)
        }else{
            if(requireContext().isNetworkAvailable()){
                viewModel.getUserAgreementFromServer()
            }else{
                showNoInternetDialog()
            }
        }

    }

    private fun showData(htmlText: String){
        dataBinding.webView.loadData(htmlText, "text/html", "UTF-8")
    }

    private fun showNoInternetDialog(){
        findNavController().apply {
            when(activity){
                is MainActivity ->  this.navigate(MainActivityDirections.actionGlobalNoInternetDialog())
                is LoginActivity -> this.navigate(LoginNavGraphDirections.actionGlobalNoInternetDialog2())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
    }

}