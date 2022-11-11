package kz.divtech.odyssey.rotation.ui.login.auth.send_sms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
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

        viewModel.employeeInfo.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { employeeData ->
                if(employeeData.data.employee?.status.equals(Constants.DEACTIVATED_EMPLOYEE))
                    showAccountDeactivatedDialog()
                else
                    viewModel.sendSmsToPhone("${Config.COUNTRY_CODE}$extractedPhoneNumber")
            }
        }

        viewModel.isEmployeeNotFounded.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { isEmployeeNotFounded ->
                if(isEmployeeNotFounded)
                    viewModel.sendSmsToPhone("${Config.COUNTRY_CODE}$extractedPhoneNumber")
            }
        }

        viewModel.isPhoneNumberFounded.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { isPhoneNumberFounded ->
                if(isPhoneNumberFounded) openCodeFragment()
                else openIINFragment()
            }
        }

        viewModel.message.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { message ->
                showErrorMessage(message)
            }
        }

        viewModel.isErrorHappened.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { isErrorHappened ->
                if(isErrorHappened) showErrorDialog()
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
            viewModel.getEmployeeInfoByPhoneNumber("${Config.COUNTRY_CODE}$extractedPhoneNumber")
        } else{
            showErrorMessage(getString(R.string.enter_phone_number_fully))
        }

    }

    private fun showErrorMessage(message: String){
        val snackBar = Snackbar.make(dataBinding.CL, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.bottom_sheet_error_title))
        snackBar.show()
    }

    private fun openCodeFragment() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToCodeFragment(dataBinding.phoneNumberET.text.toString()))

    private fun openIINFragment() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToIINFragment(dataBinding.phoneNumberET.text.toString()))

    fun showTermsOfAgreementDialog() =
         findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToTermsOfAgreementDialog())

    private fun showErrorDialog() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToPhoneNumberErrorDialog())

    private fun showAccountDeactivatedDialog() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToAccountDeactivatedDialog())

    private fun openMainActivity() = findNavController().navigate(SendSmsFragmentDirections.actionGlobalMainActivity())

}