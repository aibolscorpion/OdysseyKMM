package kz.divtech.odyssey.rotation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentProfileBinding
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager.fetchOrganizationName

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: LogoutViewModel by viewModels()
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {

        _binding = FragmentProfileBinding.inflate(inflater)
        binding.profileFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAndInsterEmployee()
        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            employee?.let {
                binding.employeeNameTV.text = it.full_name
                binding.employeeTableNumberTV.text = it.number
                binding.employeePositionTV.text = it.position
            }
        }
        binding.employeeOrgNameTV.text = fetchOrganizationName()

        binding.viewModel = viewModel
        viewModel.isSuccessfullyLoggedOut.observe(viewLifecycleOwner) {
            logout()
        }
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.profile))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ProfileFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    fun openTermsOfAgreementFragment() {
        with(findNavController()){
            if(R.id.profileFragment == currentDestination?.id)
                navigate(ProfileFragmentDirections.actionProfileFragmentToTermsOfAgreementFragment())
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
        findNavController().navigate(ProfileFragmentDirections.actionGlobalPhoneNumberFragment())
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