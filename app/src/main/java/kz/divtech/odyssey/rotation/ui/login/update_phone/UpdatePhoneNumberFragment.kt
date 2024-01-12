package kz.divtech.odyssey.rotation.ui.login.update_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.databinding.FragmentUpdatePhoneBinding
import kz.divtech.odyssey.rotation.common.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager.fetchFirebaseToken
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.UpdatePhoneRequest

@AndroidEntryPoint
class UpdatePhoneNumberFragment : Fragment() {
    private var phoneNumberFilled : Boolean = false
    private var extractedPhoneNumber: String? = null
    private var _dataBinding : FragmentUpdatePhoneBinding? = null
    private val dataBinding get() = _dataBinding!!
    val args : UpdatePhoneNumberFragmentArgs by navArgs()
    private val viewModel: UpdatePhoneViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _dataBinding = FragmentUpdatePhoneBinding.inflate(inflater)
        dataBinding.updatePhoneNumberFragment = this
        dataBinding.viewModel = viewModel
        dataBinding.employee = args.employee

        args.employee.phone?.let { changeScreenToChangePhoneNumber() }

        setupMaskedEditText()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updatePhoneResult.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Success -> {
                    showApplicationSentDialog()
                }

                is Resource.Error.HttpException.UnprocessibleEntity -> {
                    response.errorResponse.errors.forEach { (field, errorMessages) ->
                        if (field == "phone") {
                            showErrorMessage(requireContext(), dataBinding.updatePhoneNumberFL,
                                errorMessages.first())
                        }
                    }
                }

                else -> {
                    showErrorDialog()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.update_phone_number_without_auth))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "UpdatePhoneNumberFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    private fun setupMaskedEditText(){
        val maskedETListener = MaskedTextChangedListener(
            getString(R.string.phone_number_format),
            true, dataBinding.phoneNumberET, null, object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
                    phoneNumberFilled = maskFilled
                    extractedPhoneNumber = extractedValue
                }
            })

        dataBinding.phoneNumberET.addTextChangedListener(maskedETListener)
        dataBinding.phoneNumberET.onFocusChangeListener = maskedETListener
    }

    fun updatePhoneNumber(){
        if(requireContext().isNetworkAvailable()){
            if(phoneNumberFilled) {
                val request = UpdatePhoneRequest(args.employee.id,
                    "${Config.COUNTRY_CODE}$extractedPhoneNumber",
                    fetchFirebaseToken())
                viewModel.updatePhoneNumber(request)
            } else
                showErrorMessage(requireContext(), dataBinding.updatePhoneNumberFL, getString(R.string.enter_phone_number_fully))
        }else{
            showNoInternetDialog()
        }
    }

    private fun changeScreenToChangePhoneNumber(){
        dataBinding.titleTV.text = getString(R.string.change_number)
        dataBinding.descriptionTV.text = getString(R.string.do_you_want_to_change_iin)
        dataBinding.addPhoneNumberBtn.text = getString(R.string.change_phone_number)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
    }

    private fun showApplicationSentDialog(){
        with(findNavController()){
            if(R.id.updatePhoneNumber == currentDestination?.id){
                navigate(UpdatePhoneNumberFragmentDirections.actionAddPhoneNumberToApplicationSentDialog(dataBinding.phoneNumberET.text.toString()))
            }
        }
    }

    private fun showErrorDialog(){
        with(findNavController()){
            if(R.id.updatePhoneNumber == currentDestination?.id){
                navigate(UpdatePhoneNumberFragmentDirections.actionAddPhoneNumberToChangeNumberErrorDialog())
            }
        }
    }

    private fun showNoInternetDialog(){
        findNavController().navigate(UpdatePhoneNumberFragmentDirections.actionGlobalNoInternetDialog())
    }

    fun backToSearchByIINFragment() = findNavController().popBackStack()
}