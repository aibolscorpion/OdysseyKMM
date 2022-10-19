package kz.divtech.odyssey.rotation.ui.profile.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val binding = FragmentNotificationBinding.inflate(inflater)


        binding.notificationRecyclerView.adapter = NotificationAdapter(NotificationList.getList())
        return binding.root
    }
}