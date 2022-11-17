package kz.divtech.odyssey.rotation.ui.login.auth.send_sms

import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentSendSmsBinding
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import kz.divtech.odyssey.rotation.utils.Utils.showKeyboard
import kz.divtech.odyssey.rotation.ui.login.auth.AuthSharedViewModel
import kz.divtech.odyssey.rotation.utils.Utils.showErrorMessage

class SendSmsFragment : Fragment() {
    private var phoneNumberFilled : Boolean = false
    private var extractedPhoneNumber: String? = null

    private lateinit var dataBinding: FragmentSendSmsBinding
    private val viewModel  by lazy { ViewModelProvider(requireActivity())[AuthSharedViewModel::class.java] }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        if(SharedPrefs().isLoggedIn) openMainActivity()

        dataBinding = FragmentSendSmsBinding.inflate(inflater)
        dataBinding.phoneNumberFragment = this

        setupMaskedEditText()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.employeeInfo.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { employee ->
                if(employee.status.equals(Constants.DEACTIVATED_EMPLOYEE))
                    showAccountDeactivatedDialog("${employee.firstName} ${employee.patronymic}")
                else
                    viewModel.sendSmsToPhone("${Config.COUNTRY_CODE}$extractedPhoneNumber")
            }
        }

        viewModel.isEmployeeNotFounded.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { isEmployeeNotFounded ->
                if(isEmployeeNotFounded)
                    openIINFragment()
            }
        }

        viewModel.smsCodeSent.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { smsCodeSent ->
                if(smsCodeSent)
                    openCodeFragment()
            }
        }

        viewModel.message.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { message ->
                showErrorMessage(requireContext(), dataBinding.sendSmsFL, message)
            }
        }

        viewModel.isErrorHappened.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { isErrorHappened ->
                if(isErrorHappened) showErrorDialog()
            }
        }

        viewModel.showProgressBar.observe(viewLifecycleOwner){ showProgressBar ->
            dataBinding.sendSmsPB.visibility = if (showProgressBar) View.VISIBLE else View.GONE
        }

    }

    override fun onStart() {
        super.onStart()

        showKeyboard(requireContext(), dataBinding.phoneNumberET)
    }

    private fun setupMaskedEditText(){
        val prefix = getString(R.string.phone_number_placeholder)

        val maskedETListener = MaskedTextChangedListener(
        getString(R.string.phone_number_format),true, dataBinding.phoneNumberET,
            object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if(!s.toString().startsWith(prefix)){
                        dataBinding.phoneNumberET.setText(prefix)
                        Selection.setSelection(dataBinding.phoneNumberET.text, dataBinding.phoneNumberET.text!!.length)
                    }
                }

            }, object : MaskedTextChangedListener.ValueListener {
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
            showErrorMessage(requireContext(), dataBinding.sendSmsFL, getString(R.string.enter_phone_number_fully))
        }
    }



    private fun openCodeFragment() =
        findNavController().navigate(SendSmsFragmentDirections.
        actionPhoneNumberFragmentToCodeFragment(dataBinding.phoneNumberET.text.toString()))

    private fun openIINFragment() =
        findNavController().navigate(SendSmsFragmentDirections.
        actionPhoneNumberFragmentToIINFragment(dataBinding.phoneNumberET.text.toString()))

    fun showTermsOfAgreementDialog() =
         findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToTermsOfAgreementDialog())

    private fun showErrorDialog() =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToPhoneNumberErrorDialog())

    private fun showAccountDeactivatedDialog(employeeName: String) =
        findNavController().navigate(SendSmsFragmentDirections.actionPhoneNumberFragmentToAccountDeactivatedDialog(employeeName))

    private fun openMainActivity() {
        findNavController().navigate(SendSmsFragmentDirections.actionGlobalMainActivity())
        (activity as AppCompatActivity).finish()
    }

}