package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.isFailure
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.databinding.FragmentPersonalDataBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.Country
import kz.divtech.odyssey.rotation.domain.model.profile.employee.ValidationErrorResponse
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.utils.InputUtils.isEmailValid
import kz.divtech.odyssey.rotation.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.rotation.utils.Utils


class PersonalDataFragment : Fragment(), UpdatePersonalDataListener {
    val viewModel : PersonalDataViewModel by viewModels{
        PersonalDataViewModel.PersonalDataViewModelFactory((activity as MainActivity).employeeRepository)
    }
    private var _binding: FragmentPersonalDataBinding? = null
    private val binding get() = _binding!!
    private var initialCountryCode: String? = null
    private var currentEmployee: Employee? = null


    private val countrySelectionRequestKey = "countrySelectionRequestKey"
    private val countrySelectionResultKey = "countrySelectionResultKey"

    private val countrySelectionResultListener = FragmentResultListener { requestKey, bundle ->
        if (requestKey == countrySelectionRequestKey) {
            val selectedCountry = bundle.getParcelable<Country>(countrySelectionResultKey)
            selectedCountry?.let {
                currentEmployee?.country_code = it.code
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPersonalDataBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personalDataFragment = this
        binding.viewModel = viewModel

        viewModel.employee.observe(viewLifecycleOwner){
            it.let {
                currentEmployee = it
                initialCountryCode = it.country_code
                binding.employee = currentEmployee
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

        parentFragmentManager.setFragmentResultListener(
            countrySelectionRequestKey,
            viewLifecycleOwner,
            countrySelectionResultListener
        )

        viewModel.updatePersonalResult.observe(viewLifecycleOwner){ response ->
            if(response.isHttpException() &&
                (response.statusCode == Constants.UNPROCESSABLE_ENTITY_CODE)) {
                    val errorResponse = Gson().fromJson(response.error.errorBody?.string(),
                        ValidationErrorResponse::class.java)
                    errorResponse.errors.forEach{ (field, errorMessages) ->
                        val firstErrorMessage = errorMessages.first()
                        when(field){
                            "first_name" -> binding.firstNameET.error = firstErrorMessage
                            "last_name" -> binding.lastNameET.error = firstErrorMessage
                            "patronymic" -> binding.patronymicET.error = firstErrorMessage
                            "first_name_en" -> binding.firstNameEngET.error = firstErrorMessage
                            "last_name_en" -> binding.lastNameEngET.error = firstErrorMessage
                            "iin" -> binding.iinET.error = firstErrorMessage
                            "email" -> binding.emailET.error = firstErrorMessage
                        }
                    }
            }else if(response.isFailure()){
                Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
            }
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
        if(requireContext().isNetworkAvailable()){
            if(validatePersonalData()){
                currentEmployee?.let { viewModel.updatePersonalData(it, citizenshipChanged) }
            }
        }else{
            showNoInternetDialog()
        }
    }

    private fun validatePersonalData(): Boolean{
        var isValid = true

        if(binding.firstNameET.text.toString().isEmpty()){
            binding.firstNameET.error = getString(R.string.fill_your_name)
            isValid = false
        }

        if(binding.lastNameET.text.toString().isEmpty()){
            binding.lastNameET.error = getString(R.string.fill_your_surname)
            isValid = false
        }

        if(binding.iinET.text?.length != Config.IIN_LENGTH){
            binding.iinET.error = getString(R.string.enter_iin_fully)
            isValid = false
        }

        if(binding.emailET.text.toString().isNotEmpty()){
            if(!isEmailValid(binding.emailET.text.toString())){
                binding.emailET.error = getString(R.string.invalid_email_address)
                isValid = false
            }
        }
        return isValid
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

    private fun showNoInternetDialog() =
        findNavController().navigate(PersonalDataFragmentDirections.actionGlobalNoInternetDialog())

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}