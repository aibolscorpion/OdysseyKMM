package kz.divtech.odyssey.rotation.ui.login.auth.send_sms.dialogs.terms_of_agreement

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogTermsOfAgreementBinding
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener


class TermsOfAgreementDialog : BottomSheetDialogFragment(), OnCloseListener {

    val viewModel: TermsOfAgreementViewModel by lazy{ ViewModelProvider(this)[TermsOfAgreementViewModel::class.java] }


    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding  = DialogTermsOfAgreementBinding.inflate(inflater)
        dataBinding.listener = this
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserAgreement()
    }

    override fun close(){
        dismiss()
    }

}