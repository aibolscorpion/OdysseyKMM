package kz.divtech.odyssey.rotation.ui.profile.notification.notification_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogLoggedOutNotificationBinding
import kz.divtech.odyssey.rotation.ui.profile.LogoutViewModel

@AndroidEntryPoint
class LoggedOutNotificationDialog : BottomSheetDialogFragment() {
    val args: LoggedOutNotificationDialogArgs by navArgs()
    private val viewModel: LogoutViewModel by viewModels()

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogLoggedOutNotificationBinding.inflate(layoutInflater)

        binding.notification = args.notification
        binding.thisDialog = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false
    }

    private fun goToLoginPage() {
        findNavController().navigate(LoggedOutNotificationDialogDirections.actionGlobalPhoneNumberFragment())
    }

    fun deleteAndGoToLoginPage(){
        lifecycleScope.launch{
            viewModel.deleteAllDataAsync().await()
            goToLoginPage()
        }
    }
}
