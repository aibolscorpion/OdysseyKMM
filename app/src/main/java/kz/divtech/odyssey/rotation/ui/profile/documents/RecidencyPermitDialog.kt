package kz.divtech.odyssey.rotation.ui.profile.documents

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogRecidencyPermitBinding
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener


class RecidencyPermitDialog : BottomSheetDialogFragment(), SaveListener, OnCloseListener {
    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val dataBinding  = DialogRecidencyPermitBinding.inflate(inflater)
        dataBinding.closeListener = this
        dataBinding.saveListener = this

        ArrayAdapter.createFromResource(requireContext(), R.array.issue_authority_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dataBinding.issueAuthoritySpinner.adapter = adapter
        }

        return dataBinding.root
    }



    override fun onSave() {

    }

    override fun close() {
        dismiss()
    }
}