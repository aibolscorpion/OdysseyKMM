package kz.divtech.odyssey.rotation.ui.login.auth.find_by_phone_number.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants.NOTIFICATION_DATA_CONTENT
import kz.divtech.odyssey.rotation.app.Constants.NOTIFICATION_DATA_TITLE
import kz.divtech.odyssey.rotation.databinding.DialogPhoneNumberAddedBinding


class PhoneNumberAddedDialog : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val dataBinding  = DialogPhoneNumberAddedBinding.inflate(inflater)
        dataBinding.phoneNumberAddedDialog = this

        dataBinding.titleTV.text = arguments?.getString(NOTIFICATION_DATA_TITLE)
        dataBinding.contentTV.text = arguments?.getString(NOTIFICATION_DATA_CONTENT)

        return dataBinding.root
    }

}