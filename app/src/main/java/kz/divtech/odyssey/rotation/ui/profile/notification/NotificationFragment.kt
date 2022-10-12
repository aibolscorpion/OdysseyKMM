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

        val notificationList = mutableListOf(Notification("Смещение графика","Новый график: 18 апреля - 9 мая 202…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"),
            Notification("Назначен отпуск", "С 18 марта 2021 по 3 апреля 2021…", "2 ч. назад"))

        binding.notificationRecyclerView.adapter = NotificationAdapter(notificationList)
        return binding.root
    }
}