package kz.divtech.odyssey.rotation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentMainBinding
import kz.divtech.odyssey.rotation.ui.profile.notification.NotificationAdapter
import kz.divtech.odyssey.rotation.ui.profile.notification.NotificationList

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.mainFragment = this

        binding.notificationsRV.adapter = NotificationAdapter(NotificationList.getList().subList(0,3))
        return binding.root
    }


    fun openNotificationsFragment(){
        val action = MainFragmentDirections.actionMainFragmentToNotificationFragment()
        findNavController().navigate(action)
    }

    fun showCallSupportDialog(){
        val action = MainFragmentDirections.actionMainFragmentToCallSupportDialog()
        findNavController().navigate(action)
    }

    fun showWriteSupportDialog(){
        val action = MainFragmentDirections.actionMainFragmentToWriteSupportDialog()
        findNavController().navigate(action)
    }

    fun openQuestionsAnswersFragment(){
        val action = MainFragmentDirections.actionMainFragmentToQuestionsAnswersFragment()
        findNavController().navigate(action)
    }

}