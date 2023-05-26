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
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.DialogNotificationBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.PushNotification
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.utils.NetworkUtils.isNetworkAvailable

class NotificationDialog : BottomSheetDialogFragment() {
    val args: NotificationDialogArgs by navArgs()
    val viewModel: NotificationDViewModel by viewModels{
        NotificationDViewModel.NotificationDViewModelFactory(
            (activity as MainActivity).tripsRepository,
            (activity as MainActivity).notificationRepository)
    }

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

        viewModel.trip.observe(viewLifecycleOwner){
            if(it != null){
                when(args.notification.type){
                    Constants.NOTIFICATION_TYPE_APPLICATION, Constants.NOTIFICATION_TYPE_TICKET -> {
                        if(it.data.segments.isNotEmpty()){
                            findNavController().navigate(NotificationDialogDirections.actionGlobalTripDetailDialog(it.data))
                        }else{
                            findNavController().navigate(NotificationDialogDirections.actionGlobalTicketsAreNotPurchasedDialog(it.data))
                        }
                    }
                    Constants.NOTIFICATION_TYPE_REFUND_APPLICATION -> {
                        findNavController().navigate(NotificationDialogDirections.
                        actionGlobalRefundListFragment(null, it.data))
                    }
                }
            }else{
                Toast.makeText(requireContext(), R.string.trip_not_found_by_id, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun learnMore(notification: PushNotification){
        if(requireContext().isNetworkAvailable()){
            viewModel.getTripById(notification.applicationId!!)
        }else{
            showNoInternetDialog()
        }
    }

    private fun showNoInternetDialog(){
        findNavController().navigate(NotificationDialogDirections.actionGlobalNoInternetDialog())
    }

}