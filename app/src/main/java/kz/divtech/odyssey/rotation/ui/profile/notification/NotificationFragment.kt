package kz.divtech.odyssey.rotation.ui.profile.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val binding = FragmentNotificationBinding.inflate(inflater)

        binding.notificationRecyclerView.adapter = NotificationAdapter(NotificationList.getList())

        activity?.findViewById<TextView>(R.id.toolbarTitleTV)?.setText(R.string.notifications)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }
}