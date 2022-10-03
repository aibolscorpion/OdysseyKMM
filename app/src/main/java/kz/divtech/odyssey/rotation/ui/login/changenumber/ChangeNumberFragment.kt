package kz.divtech.odyssey.rotation.ui.login.changenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ChangeNumberFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun showApplicationSentDialog(){
        val action = ChangeNumberFragmentDirections.actionChangeNumberFragmentToApplicationSentDialog()
        findNavController().navigate(action)
    }

    fun showPhoneNumberAddedDialog(){
        val action = ChangeNumberFragmentDirections.actionChangeNumberFragmentToPhoneNumberAddedDialog()
        findNavController().navigate(action)
    }

    fun showErrorDialog(){
        val action = ChangeNumberFragmentDirections.actionChangeNumberFragmentToErrorHappenedDialog()
        findNavController().navigate(action)
    }


}