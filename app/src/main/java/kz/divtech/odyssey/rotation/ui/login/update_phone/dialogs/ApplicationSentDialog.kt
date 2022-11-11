package kz.divtech.odyssey.rotation.ui.login.update_phone.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogApplicationSentBinding
import kz.divtech.odyssey.rotation.ui.login.listener.DialogListener


class ApplicationSentDialog : BottomSheetDialogFragment(), DialogListener{
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val dataBinding  = DialogApplicationSentBinding.inflate(inflater)
        dataBinding.listener = this

        val args = ApplicationSentDialogArgs.fromBundle(requireArguments())
        dataBinding.phoneNumber = args.phoneNumber

        return dataBinding.root
    }

    override fun contact() = findNavController().navigate(ApplicationSentDialogDirections.actionGlobalContactSupportDialog())

    override fun close() = dismiss()

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        findNavController().navigate(ApplicationSentDialogDirections.actionApplicationSentDialogToPhoneNumberFragment())
    }
}