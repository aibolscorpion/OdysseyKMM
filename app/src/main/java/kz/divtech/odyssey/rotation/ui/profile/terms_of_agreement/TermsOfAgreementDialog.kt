package kz.divtech.odyssey.rotation.ui.profile.terms_of_agreement

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogTermsOfAgreementBinding
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
        dataBinding.termsOfAgreementTV.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun showNoInternetDialog(){
        findNavController().apply {
            this.navigate(TermsOfAgreementDialogDirections.actionGlobalNoInternetDialog())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
    }

}