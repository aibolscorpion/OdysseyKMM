package kz.divtech.odyssey.rotation.ui.help.contact_support

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogCallSupportBinding
import kz.divtech.odyssey.rotation.common.utils.ContactUtil


@AndroidEntryPoint
class CallSupportDialog : BottomSheetDialogFragment(){
    val viewModel: ContactSupportViewModel by viewModels()

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding  = DialogCallSupportBinding.inflate(inflater)
        dataBinding.thisDialog = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        lifecycleScope.launch {
            viewModel.getOrgInfoFromDB().observe(viewLifecycleOwner){
                it?.let { orgInfo ->
                    dataBinding.callSupportDesc.text =
                        getString(R.string.call_support_content, orgInfo.supportPhone)
                    dataBinding.callSupportBtn.setOnClickListener {
                        ContactUtil.callSupport(requireContext(), orgInfo.supportPhone)
                    }
                }
            }
        }

        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.call_support))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "CallSupportDialog")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

}