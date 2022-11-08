package kz.divtech.odyssey.rotation.ui.login.auth.send_sms.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener


class TermsOfAgreementDialog : BottomSheetDialogFragment(), OnCloseListener {
    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBinding  = kz.divtech.odyssey.rotation.databinding.DialogTermsOfAgreementBinding.inflate(inflater)
        dataBinding.listener = this
        return dataBinding.root
    }

    override fun close(){
        dismiss()
    }

}