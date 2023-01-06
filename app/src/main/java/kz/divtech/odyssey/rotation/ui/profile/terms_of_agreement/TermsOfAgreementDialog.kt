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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogTermsOfAgreementBinding
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener


class TermsOfAgreementDialog : BottomSheetDialogFragment(), OnCloseListener {

    val viewModel: TermsOfAgreementViewModel by lazy{ ViewModelProvider(this)[TermsOfAgreementViewModel::class.java] }
    lateinit var dataBinding: DialogTermsOfAgreementBinding

    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dataBinding  = DialogTermsOfAgreementBinding.inflate(inflater)
        dataBinding.listener = this
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
            dataBinding.webView.loadData(htmlText, "text/html", "UTF-8")
        }

        viewModel.getUserAgreement()
    }

    override fun close(){
        dismiss()
    }

}