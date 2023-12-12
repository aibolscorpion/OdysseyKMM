package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentAuthTermsBinding
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable

@AndroidEntryPoint
class TermsFragment : Fragment() {
    val viewModel: TermsViewModel by viewModels()
    private var _dataBinding: FragmentAuthTermsBinding? = null
    val dataBinding get() = _dataBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataBinding  = FragmentAuthTermsBinding.inflate(inflater)
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.accepBtn.isVisible = false
        dataBinding.termsOfAgreemntWV.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                _dataBinding?.let {
                    dataBinding.termsProgressBar.visibility = View.GONE
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?,
                                         error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                _dataBinding?.let {
                    dataBinding.termsProgressBar.visibility = View.GONE
                }
            }
        }

        viewModel.failureResult.observe(viewLifecycleOwner){ result ->
            Toast.makeText(requireContext(), "$result", Toast.LENGTH_SHORT).show()
        }

        viewModel.text.observe(viewLifecycleOwner){ text ->
            showData(text)
        }

        if(viewModel.getFile().exists()){
            lifecycleScope.launch{
                viewModel.readFile()
            }
        }else{
            if(requireContext().isNetworkAvailable()){
                viewModel.getUserAgreementFromServer()
            }else{
                findNavController().popBackStack()
                showNoInternetDialog()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.terms_of_agreement))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "TermsOfAgreementDialog")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    private fun showData(htmlText: String){
        dataBinding.termsOfAgreemntWV.loadDataWithBaseURL(null, htmlText, "text/html", "utf-8", null)
    }

    private fun showNoInternetDialog(){
        findNavController().apply {
            this.navigate(TermsFragmentDirections.actionGlobalNoInternetDialog())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
    }

}