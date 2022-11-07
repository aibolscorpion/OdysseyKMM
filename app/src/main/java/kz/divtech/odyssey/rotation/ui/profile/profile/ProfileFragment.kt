package kz.divtech.odyssey.rotation.ui.profile.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentProfileBinding
import kz.divtech.odyssey.rotation.utils.SessionManager

class ProfileFragment : Fragment() {
    private lateinit var viewModel : ProfileViewModel
    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {

        binding = FragmentProfileBinding.inflate(inflater)
        binding.profileFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.isSuccessfullyLoggedOut.observe(viewLifecycleOwner) {
            if (it) {
                logout()
            }
        }
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
        findNavController().navigate(R.id.action_global_notificationFragment)
    }

    private fun openLoginActivity(){
        val action = ProfileFragmentDirections.actionProfileFragmentToLoginActivity()
        findNavController().navigate(action)
    }

    private fun logout(){
        SessionManager().clearAuthToken()
        openLoginActivity()
    }
}