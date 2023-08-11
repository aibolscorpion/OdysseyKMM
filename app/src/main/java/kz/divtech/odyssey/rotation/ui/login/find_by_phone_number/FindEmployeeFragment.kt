package kz.divtech.odyssey.rotation.ui.login.find_by_phone_number

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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.databinding.FragmentFindEmployeeBinding
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.profile.notification.push_notification.NotificationListener
import kz.divtech.odyssey.rotation.ui.profile.notification.push_notification.PermissionRationale
import kz.divtech.odyssey.rotation.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.utils.KeyboardUtils
import kz.divtech.odyssey.rotation.utils.KeyboardUtils.showSoftKeyboard
import kz.divtech.odyssey.rotation.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.rotation.utils.Utils.changeStatusBarColor
import kz.divtech.odyssey.rotation.utils.Utils.hideBottomNavigation
import kz.divtech.odyssey.rotation.utils.Utils.hideToolbar
import kz.divtech.odyssey.rotation.utils.Utils.setMainActivityBackgroundColor

class FindEmployeeFragment : Fragment(), NotificationListener {
    private var phoneNumberFilled : Boolean = false
    private var extractedPhoneNumber: String? = null

    private var _dataBinding: FragmentFindEmployeeBinding? = null
    val dataBinding get() = _dataBinding!!

    private val viewModel: FindEmployeeViewModel by viewModels{
        FindEmployeeViewModel.FindEmployeeViewModelFactory(
            (activity?.application as App).findEmployeeRepository,
            (activity as MainActivity).orgInfoRepository
        )
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _dataBinding = FragmentFindEmployeeBinding.inflate(inflater)
        dataBinding.phoneNumberFragment = this
        dataBinding.viewModel = viewModel

        hideBottomNavigation()
        hideToolbar()
        changeStatusBarColor(R.color.status_bar)
        setupMaskedEditText()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMainActivityBackgroundColor(R.color.status_bar)
        viewModel.getOrgInfoFromServer()

        viewModel.employeeResult.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { result ->
                if(result.isSuccess()){
                    val employeeResult = result.asSuccess().value
                    if(employeeResult.exists){
                        val employee = employeeResult.employee
                        if(employee.status == Constants.DEACTIVATED_EMPLOYEE){
                            showAccountDeactivatedDialog(employee.full_name)
                        }else{
                            openSendSmsFragment()
                        }
                    }else{
                        openIINFragment()
                    }
                }else{
                    showErrorDialog()
                }
            }
        }

        checkPermission()
    }

    override fun onStart() {
        super.onStart()

        showSoftKeyboard(requireContext(), dataBinding.phoneNumberET)
    }


    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.find_employee_by_phone_number))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "FindEmployeeFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
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
                    if(maskFilled){
                        KeyboardUtils.hideSoftKeyboard(requireContext(), dataBinding.phoneNumberET)
                    }
                    phoneNumberFilled = maskFilled
                    extractedPhoneNumber = extractedValue

                }
            })

        dataBinding.phoneNumberET.addTextChangedListener(maskedETListener)
        dataBinding.phoneNumberET.onFocusChangeListener = maskedETListener
    }

    fun findEmployee(){
        if(requireContext().isNetworkAvailable()){
            if(phoneNumberFilled) {
                viewModel.getEmployeeInfoByPhoneNumber("${Config.COUNTRY_CODE}$extractedPhoneNumber")
            } else{
                showErrorMessage(requireContext(), dataBinding.findEmployeeFL, getString(R.string.enter_phone_number_fully))
            }
        }else{
            showNoInternetDialog()
        }
    }


    private fun checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(requireContext(), POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            } else if (shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                val modalBottomSheet = PermissionRationale(this)
                activity?.supportFragmentManager?.let { modalBottomSheet.show(it, "modalBottomSheet") }
            } else {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
    }

    private fun openSendSmsFragment() =
        with(findNavController()) {
            if (R.id.phoneNumberFragment == currentDestination?.id) {
                navigate(
                    FindEmployeeFragmentDirections.actionPhoneNumberFragmentToCodeFragment(
                        "${Config.COUNTRY_CODE}$extractedPhoneNumber",
                        dataBinding.phoneNumberET.text.toString()
                    )
                )
            }
        }

    private fun openIINFragment() =
        with(findNavController()) {
            if (R.id.phoneNumberFragment == currentDestination?.id) {
                navigate(
                    FindEmployeeFragmentDirections.actionPhoneNumberFragmentToIINFragment(
                        dataBinding.phoneNumberET.text.toString()
                    )
                )
            }
        }

    private fun showErrorDialog(){
        with(findNavController()){
            if(R.id.phoneNumberFragment == currentDestination?.id){
                navigate(FindEmployeeFragmentDirections.actionPhoneNumberFragmentToPhoneNumberErrorDialog())
            }
        }
    }

    private fun showAccountDeactivatedDialog(employeeName: String){
        with(findNavController()){
            if(R.id.phoneNumberFragment == currentDestination?.id){
                navigate(
                    FindEmployeeFragmentDirections.actionPhoneNumberFragmentToAccountDeactivatedDialog(
                        employeeName
                    )
                )
            }
        }
    }

    private fun showNoInternetDialog(){
        findNavController().navigate(FindEmployeeFragmentDirections.actionGlobalNoInternetDialog())
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClickOk() {
        requestPermissionLauncher.launch(POST_NOTIFICATIONS)
    }

}