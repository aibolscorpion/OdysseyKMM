package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentPersonalDataBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.Country
import kz.divtech.odyssey.rotation.domain.model.profile.CountryList
import kz.divtech.odyssey.rotation.ui.MainActivity


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

        val countries = getCountryListFromRaw()
        viewModel.employee.observe(viewLifecycleOwner){
            it?.let {
                currentEmployee = it
                initialCountryCode = it.country_code
                binding.employee = it
                setCountryNameByCode(countries)?.let {
                    binding.countryTV.text = it
                }
            }
        }

        binding.countryTV.setOnClickListener {
            openCountryListFragment(countries)
        }

        viewModel.personalDataUpdated.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), R.string.data_was_successfully_updated, Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }

        viewModel.selectedCountry.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                binding.countryTV.text = it.name
                currentEmployee?.country_code = it.code
            }
        }
    }

    private fun getCountryListFromRaw(): List<Country>{
        val jsonString = App.appContext.resources.openRawResource(R.raw.countries).bufferedReader().
        use { it.readText() }
        return Gson().fromJson(jsonString, CountryList::class.java).countries
    }

    private fun setCountryNameByCode(countryList: List<Country>): String? {
        countryList.forEachIndexed { _, country ->
            if (country.code == currentEmployee?.country_code) {
                return country.name
            }
        }
        return null
    }

    fun updatePersonalData(){
        if(initialCountryCode != currentEmployee?.country_code){
            showDefaultDocumentChangedDialog()
        }else{
            update()
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

    fun openPhoneNumberFragment(){
        findNavController().navigate(
            PersonalDataFragmentDirections.actionPersonalDataFragmentToPhoneNumberFragment2())
    }

    override fun update() {
        currentEmployee?.let { viewModel.updatePersonalData(it) }
    }

}