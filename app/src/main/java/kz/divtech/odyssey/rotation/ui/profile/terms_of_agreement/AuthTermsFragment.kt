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
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.databinding.FragmentAuthTermsBinding
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.rotation.common.utils.Utils.changeStatusBarColor
import kz.divtech.odyssey.rotation.common.utils.Utils.hideBottomNavigation
import kz.divtech.odyssey.rotation.common.utils.Utils.showToolbar


@AndroidEntryPoint
class AuthTermsFragment : Fragment() {

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

        (activity as MainActivity).binding.mainToolbar.navigationIcon = null
        showToolbar()
        hideBottomNavigation()
        changeStatusBarColor(R.color.toolbar_bg)

        dataBinding.accepBtn.setOnClickListener {
            viewModel.updateUAConfirm()
        }

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
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            openMainFragment()
        }

        viewModel.text.observe(viewLifecycleOwner){ text ->
            showData(text)
        }

        if(Config.termsOfAgreementFile.exists()){
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

        viewModel.getUaConfirmedFromDB().observe(viewLifecycleOwner){ uaConfirmed ->
            uaConfirmed?.let {
                if(it == 1L){
                    openMainFragment()
                }
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
            this.navigate(AuthTermsFragmentDirections.actionGlobalNoInternetDialog())
        }
    }

    private fun openMainFragment(){
        findNavController().navigate(AuthTermsFragmentDirections.actionGlobalMainFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
    }

}