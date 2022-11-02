package kz.divtech.odyssey.rotation.ui.login.phonenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentNumberBinding
import kz.divtech.odyssey.rotation.utils.Utils.showKeyboard
import kz.divtech.odyssey.rotation.viewmodels.AuthViewModel


class PhoneNumberFragment : Fragment() {
    private var phoneNumberFilled : Boolean = false
    private lateinit var dataBinding: FragmentNumberBinding
    private lateinit var viewModel : AuthViewModel
    private var extractedPhoneNumber: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dataBinding = FragmentNumberBinding.inflate(inflater)
        dataBinding.phoneNumberFragment = this

        setupMaskedEditText()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()

        showKeyboard(requireContext(), dataBinding.phoneNumberET)
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


    fun sendSmsToPhone(){
        if(validatePhoneNumber()) {
            viewModel.sendSmsToPhone("${Config.COUNTRY_CODE}$extractedPhoneNumber")
            openCodeFragment() 
        } else
            Toast.makeText(requireContext(), R.string.enter_phone_number_fully, Toast.LENGTH_SHORT).show()
    }

    private fun validatePhoneNumber(): Boolean = phoneNumberFilled

    private fun openCodeFragment(){
        val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToCodeFragment(dataBinding.phoneNumberET.text.toString())
        findNavController().navigate(action)
    }

    fun showTermsOfAgreementDialog(){
         val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToTermsOfAgreementDialog()
         findNavController().navigate(action)
    }

    fun showErrorDialog(){
        val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToPhoneNumberErrorDialog()
        findNavController().navigate(action)
    }

    fun showAccountDeactivatedDialog(){
        val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToAccountDeactivatedDialog()
        findNavController().navigate(action)
    }



}