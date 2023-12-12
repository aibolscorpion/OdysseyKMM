package kz.divtech.odyssey.rotation.ui.help.contact_support

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogWriteSupportBinding
import kz.divtech.odyssey.rotation.common.utils.ContactUtil

@AndroidEntryPoint
class WriteSupportDialog : BottomSheetDialogFragment() {
    val viewModel: ContactSupportViewModel by viewModels()

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val dataBinding  = DialogWriteSupportBinding.inflate(inflater)
        dataBinding.thisDialog = this
        dataBinding.contactSupport = ContactUtil

        viewModel.orgInfoLiveData.observe(viewLifecycleOwner){
            it?.let { orgInfo ->
                dataBinding.writeWhatsappBtn.setOnClickListener {
                    ContactUtil.writeSupportOnWhatsapp(this, orgInfo.whatsappPhone)
                }
                dataBinding.writeTelegramBtn.setOnClickListener{
                    orgInfo.telegramId?.let { it1 ->
                        ContactUtil.writeSupportOnTelegram(this, it1)
                    }
                }
            }
        }

        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.write_support))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "WriteSupportDialog")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

}