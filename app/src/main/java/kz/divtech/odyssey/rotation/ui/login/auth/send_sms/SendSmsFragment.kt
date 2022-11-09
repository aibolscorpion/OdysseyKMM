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
import kz.divtech.odyssey.rotation.ui.login.auth.AuthSharedViewModel


class SendSmsFragment : Fragment() {
    private var phoneNumberFilled : Boolean = false
    private var extractedPhoneNumber: String? = null

    private lateinit var dataBinding: FragmentSendSmsBinding
    private val viewModel  by lazy { ViewModelProvider(requireActivity())[AuthSharedViewModel::class.java] }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        if(SessionManager().isLoggedIn) openMainActivity()

        dataBinding = FragmentSendSmsBinding.inflate(inflater)
        dataBinding.phoneNumberFragment = this

        setupMaskedEditText()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isPhoneNumberFounded.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { isPhoneNumberFounded ->
                if(isPhoneNumberFounded) openCodeFragment() else openIINFragment()
            }
        }

        viewModel.message.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
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
        if(phoneNumberFilled) {
            viewModel.sendSmsToPhone("${Config.COUNTRY_CODE}$extractedPhoneNumber")
        } else
            Toast.makeText(requireContext(), R.string.enter_phone_number_fully, Toast.LENGTH_SHORT).show()
    }

    private fun openCodeFragment() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToCodeFragment(dataBinding.phoneNumberET.text.toString()))

    private fun openIINFragment() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToIINFragment(dataBinding.phoneNumberET.text.toString()))

    fun showTermsOfAgreementDialog() =
         findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToTermsOfAgreementDialog())

    fun showErrorDialog() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToPhoneNumberErrorDialog())

    fun showAccountDeactivatedDialog() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToAccountDeactivatedDialog())

    private fun openMainActivity() = findNavController().navigate(SendSmsFragmentDirections.actionGlobalMainActivity())

}