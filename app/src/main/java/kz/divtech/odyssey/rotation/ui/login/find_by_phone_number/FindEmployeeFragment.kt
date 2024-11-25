package kz.divtech.odyssey.rotation.ui.login.find_by_phone_number

import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentFindEmployeeBinding
import kz.divtech.odyssey.rotation.ui.MainActivityDirections
import kz.divtech.odyssey.rotation.common.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.common.utils.KeyboardUtils
import kz.divtech.odyssey.rotation.common.utils.KeyboardUtils.showSoftKeyboard
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.rotation.common.utils.Utils.changeStatusBarColor
import kz.divtech.odyssey.rotation.common.utils.Utils.hideBottomNavigation
import kz.divtech.odyssey.rotation.common.utils.Utils.hideToolbar
import kz.divtech.odyssey.rotation.common.utils.Utils.setMainActivityBackgroundColor
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import javax.inject.Inject

@AndroidEntryPoint
class FindEmployeeFragment : Fragment() {
    private var phoneNumberFilled : Boolean = false
    private var extractedPhoneNumber: String? = null

    private var _dataBinding: FragmentFindEmployeeBinding? = null
    val dataBinding get() = _dataBinding!!

    private val viewModel: FindEmployeeViewModel by viewModels()

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataBinding = FragmentFindEmployeeBinding.inflate(inflater)

        lifecycleScope.launch {
            if(dataStoreManager.isLoggedIn()){
                openMainFragment()
            }
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.phoneNumberFragment = this
        dataBinding.viewModel = viewModel

        hideBottomNavigation()
        hideToolbar()
        changeStatusBarColor(R.color.status_bar)
        setMainActivityBackgroundColor(R.color.status_bar)
        setupMaskedEditText()
        viewModel.getOrgInfoFromServer()

        viewModel.employeeResult.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { result ->
                if(result is Resource.Success){
                    val employeeResult = result.data
                    employeeResult?.let {
                        if(employeeResult.exists){
                            val employee = employeeResult.employee
                            if(employee?.status == Constants.DEACTIVATED_EMPLOYEE){
                                showAccountDeactivatedDialog(employee.fullName)
                            }else{
                                openSendSmsFragment()
                            }
                        }else{
                            openIINFragment()
                        }
                    }
                }else{
                    showErrorDialog()
                }
            }
        }


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


    private fun openMainFragment(){
        findNavController().navigate(MainActivityDirections.actionGlobalMainFragment())
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


}