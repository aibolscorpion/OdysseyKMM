package kz.divtech.odyssey.rotation.ui.profile.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentNotificationBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification

class NotificationFragment : Fragment(), NotificationListener{
    val viewModel: NotificationViewModel by viewModels {
        NotificationViewModel.NotificationViewModelFactory((activity?.application as App).notificationRepository)
    }

    lateinit var binding: FragmentNotificationBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = FragmentNotificationBinding.inflate(inflater)

        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationAdapter = NotificationAdapter(this)

        viewModel.notificationsLiveData.observe(viewLifecycleOwner) { notificationList ->
            if(notificationList.isNotEmpty()){
                notificationAdapter.setNotificationList(notificationList)
                binding.noNotifications.root.visibility = View.GONE
            }else{
                binding.noNotifications.root.visibility = View.VISIBLE
                viewModel.getNotificationsFromServer()
            }
        }
        binding.notificationRecyclerView.adapter = notificationAdapter

    }

    override fun onNotificationClicked(notification: Notification) =
        findNavController().navigate(
            NotificationFragmentDirections.actionGlobalNotificationDialog(notification))
}