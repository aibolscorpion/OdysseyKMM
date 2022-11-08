package kz.divtech.odyssey.rotation.ui.login.search_by_iin.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.ui.login.listener.DialogListener


class EmployeeNotFoundDialog : BottomSheetDialogFragment(), DialogListener {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBinding  = kz.divtech.odyssey.rotation.databinding.DialogEmployeeNotFoundBinding.inflate(inflater)
        dataBinding.listener = this

        return dataBinding.root
    }

    override fun contact() {
        findNavController().navigate(EmployeeNotFoundDialogDirections.actionGlobalContactSupportDialog())
    }

    override fun close(){
        dismiss()
    }

}