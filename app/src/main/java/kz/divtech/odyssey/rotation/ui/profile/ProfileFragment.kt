package kz.divtech.odyssey.rotation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val binding = FragmentProfileBinding.inflate(inflater)
        binding.profileFragment = this
        return binding.root
    }

    fun showTermsOfAgreement(){
        val action = ProfileFragmentDirections.actionProfileFragmentToTermsOfAgreementDialog2()
        findNavController().navigate(action)
    }

    fun openDocumentsFragment(){
        val action = ProfileFragmentDirections.actionProfileFragmentToDocumentsFragment()
        findNavController().navigate(action)
    }

    fun openPersonalDataFragment(){
        val action = ProfileFragmentDirections.actionProfileFragmentToPersonalDataFragment()
        findNavController().navigate(action)
    }

    fun openNotificationFragment(){
        val action = ProfileFragmentDirections.actionProfileFragmentToNotificationFragment()
        findNavController().navigate(action)
    }
}