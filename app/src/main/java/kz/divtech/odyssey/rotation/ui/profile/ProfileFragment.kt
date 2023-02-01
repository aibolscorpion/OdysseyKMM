package kz.divtech.odyssey.rotation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentProfileBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.utils.Utils.appendWithoutNull

class ProfileFragment : Fragment() {
    private var currentEmployee: Employee? = null
    private val viewModel: ProfileViewModel by viewModels{
        ProfileViewModel.ProfileViewModelFactory(
            (activity as MainActivity).tripsRepository,
            (activity as MainActivity).employeeRepository,
            (activity as MainActivity).faqRepository,
            (activity as MainActivity).documentRepository,
            (activity as MainActivity).newsRepository,
            (activity as MainActivity).articleRepository,
            (activity as MainActivity).notificationRepository)
    }
    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {

        binding = FragmentProfileBinding.inflate(inflater)
        binding.profileFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            employee?.let { it ->
                currentEmployee = it
                binding.employeeNameTV.text = StringBuilder().appendWithoutNull(it.lastName).
                append(Constants.SPACE).appendWithoutNull(it.firstName).append(Constants.SPACE).
                appendWithoutNull(it.patronymic)
                binding.employeeTableNumberTV.text = it.number
                binding.employeeOrgNameTV.text = it.orgName
                binding.employeePositionTV.text = it.position
            }

        }

        binding.viewModel = viewModel
        viewModel.isSuccessfullyLoggedOut.observe(viewLifecycleOwner) {
            logout()
        }
    }

    fun showTermsOfAgreement() =
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToTermsOfAgreementDialog())

    fun openDocumentsFragment() =
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToDocumentsFragment())

    fun openPersonalDataFragment(){
        val action = ProfileFragmentDirections.actionProfileFragmentToPersonalDataFragment(currentEmployee!!)
        findNavController().navigate(action)
    }

    fun openNotificationFragment() = findNavController().navigate(R.id.action_global_notificationFragment)

    private fun goToLoginPage() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginActivity())
        (activity as AppCompatActivity).finish()
    }

    private fun logout(){
        lifecycleScope.launch{
            viewModel.deleteAllDataAsync().await()
            goToLoginPage()
        }
    }
}