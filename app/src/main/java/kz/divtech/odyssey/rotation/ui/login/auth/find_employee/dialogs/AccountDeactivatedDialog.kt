package kz.divtech.odyssey.rotation.ui.login.auth.find_employee.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogAccountDeactivatedBinding
import kz.divtech.odyssey.rotation.ui.login.listener.DialogListener


class AccountDeactivatedDialog : BottomSheetDialogFragment(), DialogListener {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val dataBinding  = DialogAccountDeactivatedBinding.inflate(inflater)
        dataBinding.listener = this

        dataBinding.employeeName = AccountDeactivatedDialogArgs.fromBundle(requireArguments()).employeeName

        return dataBinding.root
    }

    override fun contact() =
        findNavController().navigate(AccountDeactivatedDialogDirections.actionGlobalContactSupportDialog())


    override fun close(){
        dismiss()
    }

}