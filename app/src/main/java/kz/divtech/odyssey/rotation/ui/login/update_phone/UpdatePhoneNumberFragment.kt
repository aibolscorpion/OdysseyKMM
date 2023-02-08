package kz.divtech.odyssey.rotation.ui.login.update_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.databinding.FragmentUpdatePhoneBinding
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import kz.divtech.odyssey.rotation.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.utils.SharedPrefs

class UpdatePhoneNumberFragment : Fragment() {
    private var phoneNumberFilled : Boolean = false
    private var extractedPhoneNumber: String? = null
    private lateinit var dataBinding : FragmentUpdatePhoneBinding
    private val employee by lazy { UpdatePhoneNumberFragmentArgs.fromBundle(requireArguments()).employee }
    private val viewModel  by lazy { ViewModelProvider(this)[UpdatePhoneViewModel::class.java] }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        dataBinding = FragmentUpdatePhoneBinding.inflate(inflater)
        dataBinding.updatePhoneNumberFragment = this

        dataBinding.employee = employee

        if(employee.isPhoneNumber!!){
            changeScreenToChangePhoneNumber()
        }

        setupMaskedEditText()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isApplicationSent.observe(viewLifecycleOwner) { isApplicationSent ->
            if(isApplicationSent)
                showApplicationSentDialog()
            else
                showErrorDialog()
        }
        viewModel.message.observe(viewLifecycleOwner){ message ->
            showErrorMessage(requireContext(), dataBinding.updatePhoneNumberFL, message)
        }
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
        if(phoneNumberFilled) {
            val request = UpdatePhoneRequest(employee.id, employee.number, employee.firstName, employee.lastName,
                employee.iin, "${Config.COUNTRY_CODE}$extractedPhoneNumber",
                SharedPrefs.fetchFirebaseToken(requireContext()))
            viewModel.updatePhoneNumber(request)
        } else
            showErrorMessage(requireContext(), dataBinding.updatePhoneNumberFL, getString(R.string.enter_phone_number_fully))
    }

    private fun changeScreenToChangePhoneNumber(){
        dataBinding.titleTV.text = getString(R.string.change_number)
        dataBinding.descriptionTV.text = getString(R.string.do_you_want_to_change_iin)
        dataBinding.addPhoneNumberBtn.text = getString(R.string.change_phone_number)
    }

    private fun showApplicationSentDialog() =
        findNavController().navigate(UpdatePhoneNumberFragmentDirections.actionAddPhoneNumberToApplicationSentDialog(dataBinding.phoneNumberET.text.toString()))

    private fun showErrorDialog() =
        findNavController().navigate(UpdatePhoneNumberFragmentDirections.actionAddPhoneNumberToChangeNumberErrorDialog())

    fun backToSearchByIINFragment() = findNavController().popBackStack()
}