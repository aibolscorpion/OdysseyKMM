package kz.divtech.odyssey.rotation.ui.profile.notification.notification_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.databinding.DialogNotificationBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.PushNotification
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable

@AndroidEntryPoint
class NotificationDialog : BottomSheetDialogFragment() {
    val args: NotificationDialogArgs by navArgs()
    val viewModel: NotificationDViewModel by viewModels()

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DialogNotificationBinding.inflate(layoutInflater)
        binding.notification = args.notification
        binding.thisDialog = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.markNotificationAsRead(args.notification.id)
        isCancelable = !args.notification.isImportant

        viewModel.tripResult.observe(viewLifecycleOwner){ result ->
            if(result.isSuccess()){
                val trip = result.asSuccess().value
                if(trip.data != null){
                    when(args.notification.type){
                        Constants.NOTIFICATION_TYPE_APPLICATION, Constants.NOTIFICATION_TYPE_TICKET -> {
                            if(trip.data.segments.isNotEmpty()){
                                findNavController().navigate(NotificationDialogDirections.actionGlobalTripDetailDialog(trip.data))
                            }else{
                                findNavController().navigate(NotificationDialogDirections.actionGlobalTicketsAreNotPurchasedDialog(trip.data))
                            }
                        }
                        Constants.NOTIFICATION_TYPE_REFUND_APPLICATION -> {
                            findNavController().navigate(NotificationDialogDirections.
                                actionGlobalRefundListFragment(null, trip.data))
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), R.string.trip_not_found_by_id, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "$result", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun learnMore(notification: PushNotification){
        if(requireContext().isNetworkAvailable()){
            notification.applicationId?.let { viewModel.getTripById(it) }
        }else{
            showNoInternetDialog()
        }
    }

    private fun showNoInternetDialog(){
        findNavController().navigate(NotificationDialogDirections.actionGlobalNoInternetDialog())
    }

}