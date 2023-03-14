package kz.divtech.odyssey.rotation.ui.profile.notification.push_notification

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogPermissionRationaleBinding

class PermissionRationale(val listener: NotificationListener) : BottomSheetDialogFragment(){
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding  = DialogPermissionRationaleBinding.inflate(inflater)
        dataBinding.thisDialog = this
        dataBinding.okBtn.setOnClickListener {
            dismiss()
            listener.onClickOk()
        }
        return dataBinding.root
    }

}
interface NotificationListener{
    fun onClickOk()
}