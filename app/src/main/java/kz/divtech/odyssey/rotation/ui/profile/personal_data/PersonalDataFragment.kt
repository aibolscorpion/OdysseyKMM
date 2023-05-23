package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentPersonalDataBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.Country
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.utils.Utils


class PersonalDataFragment : Fragment(), UpdatePersonalDataListener {
    val viewModel : PersonalDataViewModel by activityViewModels{
        PersonalDataViewModel.PersonalDataViewModelFactory((activity as MainActivity).employeeRepository)
    }
    private var _binding: FragmentPersonalDataBinding? = null
    private val binding get() = _binding!!
    private var initialCountryCode: String? = null
    private var currentEmployee: Employee? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPersonalDataBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personalDataFragment = this
        binding.viewModel = viewModel

        viewModel.employee.observeOnce(viewLifecycleOwner){
            it.let {
                currentEmployee = it
                initialCountryCode = it.country_code
                binding.employee = it
            }
        }

        binding.countryTV.setOnClickListener {
            openCountryListFragment(Utils.getCountryList())
        }

        viewModel.personalDataUpdated.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { citizenshipChanged ->
                Toast.makeText(requireContext(), R.string.data_was_successfully_updated, Toast.LENGTH_LONG).show()
                if(citizenshipChanged){
                    openDocumentsFragment()
                }else{
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.selectedCountry.observe(viewLifecycleOwner) {
            binding.countryTV.text = it.name
            currentEmployee?.country_code = it.code
        }
    }
    private fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
        observe(owner, object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        })
    }

    fun checkCitizenshipAndUpdate(){
        if(initialCountryCode != currentEmployee?.country_code){
            showDefaultDocumentChangedDialog()
        }else{
            updatePersonalData(false)
        }
    }
    private fun showDefaultDocumentChangedDialog(){
        val dialog = DefDocumentChangedDialog(this)
        activity?.supportFragmentManager?.let { dialog.show(it, "DefaultDocumentChangedDialog") }
    }

    private fun openCountryListFragment(countryList: List<Country>){
        currentEmployee?.country_code?.let {
            findNavController().navigate(PersonalDataFragmentDirections.
            actionPersonalDataFragmentToCountryListFragment(countryList.toTypedArray(), it))
        }
    }

    override fun updatePersonalData(citizenshipChanged: Boolean) {
        currentEmployee?.let { viewModel.updatePersonalData(it, citizenshipChanged) }
    }

    fun openPhoneNumberFragment(){
        findNavController().navigate(
            PersonalDataFragmentDirections.actionPersonalDataFragmentToPhoneNumberFragment2())
    }

    private fun openDocumentsFragment(){
        findNavController().navigate(
            PersonalDataFragmentDirections.actionPersonalDataFragmentToDocumentsFragment()
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.viewModelStore?.clear()
    }

}