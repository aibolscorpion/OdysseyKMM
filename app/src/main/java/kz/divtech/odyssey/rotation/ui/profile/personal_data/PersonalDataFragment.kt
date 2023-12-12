package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.data.remote.result.isFailure
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.databinding.FragmentPersonalDataBinding
import kz.divtech.odyssey.rotation.domain.model.profile.Country
import kz.divtech.odyssey.rotation.domain.model.errors.ValidationErrorResponse
import kz.divtech.odyssey.rotation.common.utils.InputUtils.isEmailValid
import kz.divtech.odyssey.rotation.common.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.rotation.common.utils.Utils

@AndroidEntryPoint
class PersonalDataFragment : Fragment(), UpdatePersonalDataListener {
    val viewModel : PersonalDataViewModel by viewModels()
    private var _binding: FragmentPersonalDataBinding? = null
    private val binding get() = _binding!!
    private var initialCountryCode: String? = null

    private val countrySelectionRequestKey = "countrySelectionRequestKey"
    private val countrySelectionResultKey = "countrySelectionResultKey"

    @Suppress("DEPRECATION")
    private val countrySelectionResultListener = FragmentResultListener { requestKey, bundle ->
        if (requestKey == countrySelectionRequestKey) {
            val selectedCountry = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(countrySelectionResultKey, Country::class.java)
            } else {
                bundle.getParcelable(countrySelectionResultKey) as Country?
            }
            selectedCountry?.let {
                viewModel.employee.value?.country_code = it.code
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
            it?.let {
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

        parentFragmentManager.setFragmentResultListener(
            countrySelectionRequestKey,
            viewLifecycleOwner,
            countrySelectionResultListener
        )

        viewModel.updatePersonalResult.observe(viewLifecycleOwner){ response ->
            if(response.isHttpException() &&
                (response.statusCode == Constants.UNPROCESSABLE_ENTITY_CODE)) {
                    showErrorMessage(requireContext(), binding.personalDataFL, getString(R.string.invalid_data))
                    val errorResponse = Gson().fromJson(response.error.errorBody?.string(),
                        ValidationErrorResponse::class.java)
                    errorResponse.errors.forEach{ (field, errorMessages) ->
                        val firstErrorMessage = errorMessages.first()
                        when(field){
                            "first_name" -> {
                                binding.firstNameET.error = firstErrorMessage
                                binding.personalDataSV.post {
                                    binding.personalDataSV.smoothScrollTo(0, binding.firstNameTV.bottom)
                                }
                            }
                            "last_name" -> {
                                binding.lastNameET.error = firstErrorMessage
                                binding.personalDataSV.post {
                                    binding.personalDataSV.smoothScrollTo(0, binding.lastNameTV.bottom)
                                }
                            }
                            "patronymic" -> {
                                binding.patronymicET.error = firstErrorMessage
                                binding.personalDataSV.post {
                                    binding.personalDataSV.smoothScrollTo(0, binding.patronymicTV.bottom)
                                }
                            }
                            "first_name_en" -> {
                                binding.firstNameEngET.error = firstErrorMessage
                                binding.personalDataSV.post {
                                    binding.personalDataSV.smoothScrollTo(0, binding.firstNameEngTV.bottom)
                                }
                            }
                            "last_name_en" -> {
                                binding.lastNameEngET.error = firstErrorMessage
                                binding.personalDataSV.post {
                                    binding.personalDataSV.smoothScrollTo(0, binding.lastNameEngTV.bottom)
                                }
                            }
                            "iin" -> {
                                binding.iinET.error = firstErrorMessage
                            }
                            "email" -> {
                                binding.emailET.error = firstErrorMessage
                            }
                        }
                    }
            }else if(response.isFailure()){
                Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.personal_data))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "PersonalDataFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun checkCitizenshipAndUpdate(){
        if(initialCountryCode != viewModel.employee.value?.country_code){
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
        viewModel.employee.value?.country_code?.let {
            findNavController().navigate(PersonalDataFragmentDirections.
            actionPersonalDataFragmentToCountryListFragment(countryList.toTypedArray(), it))
        }
    }

    override fun updatePersonalData(citizenshipChanged: Boolean) {
        if(requireContext().isNetworkAvailable()){
            if(validatePersonalData()){
                viewModel.employee.value?.let { viewModel.updatePersonalData(it, citizenshipChanged) }
            }else{
                showErrorMessage(requireContext(), binding.personalDataFL, getString(R.string.invalid_data))
            }
        }else{
            showNoInternetDialog()
        }
    }

    private fun validatePersonalData(): Boolean{
        var isValid = true

        if(binding.emailET.text.toString().isNotEmpty()){
            if(!isEmailValid(binding.emailET.text.toString())){
                binding.emailET.error = getString(R.string.invalid_email_address)
                isValid = false
            }
        }

        if(binding.iinET.text?.length != Config.IIN_LENGTH){
            binding.iinET.error = getString(R.string.enter_iin_fully)
            isValid = false
        }

        if(binding.lastNameET.text.toString().isEmpty()){
            binding.lastNameET.error = getString(R.string.fill_your_surname)
            binding.personalDataSV.post {
                binding.personalDataSV.smoothScrollTo(0, binding.lastNameTV.bottom)
            }
            isValid = false
        }

        if(binding.firstNameET.text.toString().isEmpty()){
            binding.firstNameET.error = getString(R.string.fill_your_name)
            binding.personalDataSV.post {
                binding.personalDataSV.smoothScrollTo(0, binding.firstNameTV.bottom)
            }
            isValid = false
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