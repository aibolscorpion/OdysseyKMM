package kz.divtech.odyssey.rotation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentProfileBinding
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.utils.SharedPrefs

class ProfileFragment : Fragment() {
    private val viewModel: LogoutViewModel by viewModels{
        LogoutViewModel.LogoutViewModelFactory(
            (activity as MainActivity).tripsRepository,
            (activity as MainActivity).employeeRepository,
            (activity as MainActivity).faqRepository,
            (activity as MainActivity).newsRepository,
            (activity as MainActivity).articleRepository,
            (activity as MainActivity).notificationRepository,
            (activity as MainActivity).orgInfoRepository)
    }
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {

        _binding = FragmentProfileBinding.inflate(inflater)
        binding.profileFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            employee?.let {
                binding.employeeNameTV.text = it.full_name
                binding.employeeTableNumberTV.text = it.number
                binding.employeePositionTV.text = it.position
            }
        }
        binding.employeeOrgNameTV.text = SharedPrefs.fetchOrganizationName(requireContext())

        binding.viewModel = viewModel
        viewModel.isSuccessfullyLoggedOut.observe(viewLifecycleOwner) {
            logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    fun showTermsOfAgreement() {
        with(findNavController()){
            if(R.id.profileFragment == currentDestination?.id)
                navigate(ProfileFragmentDirections.actionProfileFragmentToTermsOfAgreementDialog())
        }
    }

    fun openDocumentsFragment() =
        with(findNavController()) {
            if (R.id.profileFragment == currentDestination?.id)
                navigate(ProfileFragmentDirections.actionProfileFragmentToDocumentsFragment())
        }


    fun openPersonalDataFragment(){
        with(findNavController()) {
            if (R.id.profileFragment == currentDestination?.id)
                navigate(ProfileFragmentDirections.actionProfileFragmentToPersonalDataFragment())
        }
    }

    fun openNotificationFragment() =
        with(findNavController()) {
            if (R.id.profileFragment == currentDestination?.id)
                navigate(R.id.action_global_notificationFragment)
        }

    private fun goToLoginPage() {
        findNavController().navigate(ProfileFragmentDirections.actionGlobalLoginActivity())
        (activity as AppCompatActivity).finish()
    }

    private fun logout(){
        lifecycleScope.launch{
            binding.logoutProgressBar.isVisible = true
            viewModel.deleteAllDataAsync().await()
            binding.logoutProgressBar.isVisible = false
            goToLoginPage()
        }
    }
}