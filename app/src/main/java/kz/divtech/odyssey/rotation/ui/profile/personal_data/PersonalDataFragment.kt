package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentPersonalDataBinding
import kz.divtech.odyssey.rotation.domain.model.profile.Country
import kz.divtech.odyssey.rotation.domain.model.profile.CountryList
import kz.divtech.odyssey.rotation.ui.MainActivity


class PersonalDataFragment : Fragment(), UpdatePersonalDataListener {
    val viewModel : PersonalDataViewModel by activityViewModels{
        PersonalDataViewModel.PersonalDataViewModelFactory((activity as MainActivity).employeeRepository)
    }
    private var _binding: FragmentPersonalDataBinding? = null
    private val binding get() = _binding!!
    val args: PersonalDataFragmentArgs by navArgs()
    private var initialCountryCode: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPersonalDataBinding.inflate(inflater)

        binding.employee = args.employee
        binding.personalDataFragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialCountryCode = args.employee.country_code
        val countries = getCountryListFromRaw()
        setCountryNameByCode(countries)?.let {
            binding.countryTV.text = it
        }
        binding.countryTV.setOnClickListener {
            openCountryListFragment(countries)
        }
        viewModel.selectedCountry.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                binding.countryTV.text = it.name
                args.employee.country_code = it.code
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
            if (country.code == args.employee.country_code) {
                return country.name
            }
        }
        return null
    }

    fun updatePersonalData(){
        if(initialCountryCode != args.employee.country_code){
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
        findNavController().navigate(PersonalDataFragmentDirections.
            actionPersonalDataFragmentToCountryListFragment(countryList.toTypedArray(),
                args.employee.country_code))
    }

    override fun update() {
        viewModel.updatePersonalData(args.employee)
    }

}