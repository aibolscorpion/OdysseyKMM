package kz.divtech.odyssey.rotation.ui.login.change_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentChangeNumberBinding

class ChangePhoneNumberFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = FragmentChangeNumberBinding.inflate(inflater)
        binding.changeNumberFragment = this

        val args = ChangePhoneNumberFragmentArgs.fromBundle(requireArguments())
        binding.employee = args.employee

        return binding.root
    }

    fun showApplicationSentDialog(){
        findNavController().navigate(ChangePhoneNumberFragmentDirections.actionChangeNumberFragmentToApplicationSentDialog())
    }

    fun showPhoneNumberAddedDialog(){
        findNavController().navigate(ChangePhoneNumberFragmentDirections.actionChangeNumberFragmentToPhoneNumberAddedDialog())
    }

    fun showErrorDialog(){
        findNavController().navigate(ChangePhoneNumberFragmentDirections.actionChangeNumberFragmentToChangeNumberErrorDialog())
    }

    fun backToSearchByIINFragment(){
        findNavController().popBackStack()
    }

}