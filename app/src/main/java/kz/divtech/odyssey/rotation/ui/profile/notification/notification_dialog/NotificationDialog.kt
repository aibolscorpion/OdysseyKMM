package kz.divtech.odyssey.rotation.ui.profile.notification.notification_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.DialogNotificationBinding

class NotificationDialog : BottomSheetDialogFragment() {
    val viewModel: NotificationDViewModel by viewModels{
        NotificationDViewModel.NotificationDViewModelFactory(
            (activity?.application as App).tripsRepository,
            (activity?.application as App).notificationRepository)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DialogNotificationBinding.inflate(layoutInflater)

        val args = NotificationDialogArgs.fromBundle(requireArguments())
        binding.notification = args.notification
        binding.thisDialog = this
        binding.viewModel = viewModel

        viewModel.markNotificationAsRead(args.notification.id)

        return binding.root
    }

    fun learnMore(tripId: Int){
        viewModel.getTripById(tripId).observe(viewLifecycleOwner){ trip ->
            if(trip != null){
                if(trip.segments != null){
                    findNavController().navigate(NotificationDialogDirections.actionGlobalTripDetailDialog(trip))
                }else{
                    findNavController().navigate(NotificationDialogDirections.actionGlobalTicketsAreNotPurchasedDialog(trip))
                }
            }else{
                Toast.makeText(requireContext(), R.string.trip_not_found_by_id, Toast.LENGTH_SHORT).show()
            }

        }
    }


}