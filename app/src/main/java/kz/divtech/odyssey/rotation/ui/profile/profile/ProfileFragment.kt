package kz.divtech.odyssey.rotation.ui.profile.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentProfileBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import kz.divtech.odyssey.rotation.utils.Utils.appendWithoutNull

class ProfileFragment : Fragment() {
    var currentEmployee: Employee? = null
    private val viewModel: ProfileViewModel by viewModels(){
        ProfileViewModel.ProfileViewModelFactory((activity?.application as App).repository)
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
            currentEmployee = employee
            binding.employeeNameTV.text = StringBuilder().appendWithoutNull(employee.lastName).
                append(Constants.SPACE).appendWithoutNull(employee.firstName).append(Constants.SPACE).
                appendWithoutNull(employee.patronymic)
            binding.employeeTableNumberTV.text = employee.number
            binding.employeeOrgNameTV.text = employee.orgName
            binding.employeePositionTV.text = employee.position
        }

        binding.viewModel = viewModel
        viewModel.isSuccessfullyLoggedOut.observe(viewLifecycleOwner) {
            if (it) {
                logout()
            }
        }
    }

    fun showTermsOfAgreement() =
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToTermsOfAgreementDialog2())

    fun openDocumentsFragment() =
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToDocumentsFragment())

    fun openPersonalDataFragment(){
        val action = ProfileFragmentDirections.actionProfileFragmentToPersonalDataFragment(currentEmployee!!)
        findNavController().navigate(action)
    }

    fun openNotificationFragment() = findNavController().navigate(R.id.action_global_notificationFragment)

    private fun openLoginActivity() =
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginActivity())

    private fun logout(){
        SharedPrefs().clearAuthToken()
        openLoginActivity()
        (activity as AppCompatActivity).finish()
    }
}