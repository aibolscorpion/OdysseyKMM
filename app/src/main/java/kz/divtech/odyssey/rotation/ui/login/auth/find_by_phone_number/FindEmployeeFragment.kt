package kz.divtech.odyssey.rotation.ui.login.auth.find_by_phone_number

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentFindEmployeeBinding
import kz.divtech.odyssey.rotation.ui.push_notification.NotificationListener
import kz.divtech.odyssey.rotation.ui.push_notification.PermissionRationale
import kz.divtech.odyssey.rotation.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.utils.KeyboardUtils.showKeyboard

class FindEmployeeFragment : Fragment(), NotificationListener {
    private var phoneNumberFilled : Boolean = false
    private var extractedPhoneNumber: String? = null

    private lateinit var dataBinding: FragmentFindEmployeeBinding
    private val viewModel: FindEmployeeViewModel by viewModels{
        FindEmployeeViewModel.FindEmployeeViewModelFactory(
            (activity?.application as App).findEmployeeRepository,
            (activity?.application as App).orgInfoRepository)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        dataBinding = FragmentFindEmployeeBinding.inflate(inflater)
        dataBinding.phoneNumberFragment = this

        dataBinding.viewModel = viewModel

        setupMaskedEditText()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getOrgInfoFromServer()

        viewModel.employeeInfo.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { employee ->
                if(employee.status.equals(Constants.DEACTIVATED_EMPLOYEE))
                    showAccountDeactivatedDialog("${employee.firstName} ${employee.patronymic}")
                else
                    openSendSmsFragment()
            }
        }

        viewModel.isEmployeeNotFounded.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { isEmployeeNotFounded ->
                if(isEmployeeNotFounded)
                    openIINFragment()
            }
        }

        viewModel.isErrorHappened.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { isErrorHappened ->
                if(isErrorHappened)
                    showErrorDialog()
            }
        }

        checkPermission()

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


    fun findEmployee(){
        if(phoneNumberFilled) {
            viewModel.getEmployeeInfoByPhoneNumber("${Config.COUNTRY_CODE}$extractedPhoneNumber")
        } else{
            showErrorMessage(requireContext(), dataBinding.findEmployeeFL, getString(R.string.enter_phone_number_fully))
        }
    }


    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(requireContext(), POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            } else if (shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                val modalBottomSheet = PermissionRationale(this)
                modalBottomSheet.show(activity?.supportFragmentManager!!, "modalBottomSheet")
            } else {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }

    private fun openSendSmsFragment() =
        findNavController().navigate(FindEmployeeFragmentDirections.
        actionPhoneNumberFragmentToCodeFragment("${Config.COUNTRY_CODE}$extractedPhoneNumber",
            dataBinding.phoneNumberET.text.toString()))

    private fun openIINFragment() =
        findNavController().navigate(FindEmployeeFragmentDirections.
        actionPhoneNumberFragmentToIINFragment(dataBinding.phoneNumberET.text.toString()))

    fun showTermsOfAgreementDialog() =
         findNavController().navigate(FindEmployeeFragmentDirections.actionPhoneNumberFragmentToTermsOfAgreementDialog())

    private fun showErrorDialog() =
        findNavController().navigate(FindEmployeeFragmentDirections.actionPhoneNumberFragmentToPhoneNumberErrorDialog())

    private fun showAccountDeactivatedDialog(employeeName: String) =
        findNavController().navigate(FindEmployeeFragmentDirections.actionPhoneNumberFragmentToAccountDeactivatedDialog(employeeName))

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClickOk() {
        requestPermissionLauncher.launch(POST_NOTIFICATIONS)
    }

}