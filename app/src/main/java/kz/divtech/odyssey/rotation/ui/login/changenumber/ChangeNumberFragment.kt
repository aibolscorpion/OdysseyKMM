package kz.divtech.odyssey.rotation.ui.login.changenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentChangeNumberBinding

class ChangeNumberFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChangeNumberBinding.inflate(inflater)
        binding.changeNumberFragment = this
        showApplicationSentDialog()
        return binding.root
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
        val action = ChangeNumberFragmentDirections.actionChangeNumberFragmentToChangeNumberErrorDialog()
        findNavController().navigate(action)
    }

    fun openConfirmDataFragment(){
        val action = ChangeNumberFragmentDirections.actionChangeNumberFragmentToConfirmData()
        findNavController().navigate(action)
    }

}