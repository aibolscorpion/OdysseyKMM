package kz.divtech.odyssey.rotation.ui.help.contact_support

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogCallSupportBinding
import kz.divtech.odyssey.rotation.ui.MainActivity


class CallSupportDialog : BottomSheetDialogFragment(){
    val viewModel: ContactSupportViewModel by viewModels{
        ContactSupportViewModel.ContactSupportViewModelFactory(
            (activity as MainActivity).orgInfoRepository)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding  = DialogCallSupportBinding.inflate(inflater)
        dataBinding.thisDialog = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewModel.orgInfoLiveData.observe(viewLifecycleOwner){
            it?.let { orgInfo ->
                dataBinding.callSupportDesc.text =
                    getString(R.string.call_support_content, orgInfo.supportPhone)

                dataBinding.callSupportBtn.setOnClickListener {
                    ContactUtil.callSupport(this, orgInfo.supportPhone)
                }
            }
        }

        return dataBinding.root
    }

}