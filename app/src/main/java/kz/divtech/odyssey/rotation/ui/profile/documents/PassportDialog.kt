package kz.divtech.odyssey.rotation.ui.profile.documents

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogPassportBinding
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener


class PassportDialog : BottomSheetDialogFragment(), SaveListener, OnCloseListener {
    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBinding  = DialogPassportBinding.inflate(inflater)
        dataBinding.closeListener = this
        dataBinding.saveListener = this
        return dataBinding.root
    }


    override fun onSave(){

    }

    override fun close() {
        dismiss()
    }

}