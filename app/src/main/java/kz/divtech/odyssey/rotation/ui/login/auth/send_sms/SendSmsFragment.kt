package kz.divtech.odyssey.rotation.ui.login.auth.send_sms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentSendSmsBinding
import kz.divtech.odyssey.rotation.utils.SessionManager
import kz.divtech.odyssey.rotation.utils.Utils.showKeyboard
import kz.divtech.odyssey.rotation.ui.login.auth.AuthViewModel


class SendSmsFragment : Fragment() {
    private var phoneNumberFilled : Boolean = false
    private lateinit var dataBinding: FragmentSendSmsBinding
    private val viewModel  by lazy { ViewModelProvider(requireActivity())[AuthViewModel::class.java] }
    private var extractedPhoneNumber: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        if(SessionManager().isLoggedIn){
            findNavController().navigate(SendSmsFragmentDirections.actionGlobalMainActivity())
        }

        dataBinding = FragmentSendSmsBinding.inflate(inflater)
        dataBinding.phoneNumberFragment = this

        setupMaskedEditText()


        openIINFragment("+7(778)554 43 72")

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToCodeFragment(dataBinding.phoneNumberET.text.toString()))
    }

    private fun openIINFragment(phoneNumber: String){
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToIINFragment(phoneNumber))
    }

    fun showTermsOfAgreementDialog(){
         findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToTermsOfAgreementDialog())
    }

    fun showErrorDialog(){
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToPhoneNumberErrorDialog())
    }

    fun showAccountDeactivatedDialog(){
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToAccountDeactivatedDialog())
    }

}