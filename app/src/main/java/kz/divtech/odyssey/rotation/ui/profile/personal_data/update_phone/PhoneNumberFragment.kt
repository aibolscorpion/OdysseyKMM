package kz.divtech.odyssey.rotation.ui.profile.personal_data.update_phone

import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.databinding.FragmentUpdatePhone2Binding
import kz.divtech.odyssey.rotation.utils.InputUtils
import kz.divtech.odyssey.rotation.utils.KeyboardUtils

class PhoneNumberFragment: Fragment() {
    private var phoneNumberFilled : Boolean = false
    private var extractedPhoneNumber: String? = null

    private var _binding: FragmentUpdatePhone2Binding? = null
    internal val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUpdatePhone2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPhoneNumber()
        setupMaskedEditText()
    }


    override fun onStart() {
        super.onStart()

        KeyboardUtils.showKeyboard(requireContext(), binding.phoneNumberET)
    }

    private fun setupMaskedEditText(){
        val prefix = getString(R.string.phone_number_placeholder)

        val maskedETListener = MaskedTextChangedListener(
            getString(R.string.phone_number_format),true, binding.phoneNumberET,
            object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if(!s.toString().startsWith(prefix)){
                        binding.phoneNumberET.setText(prefix)
                        Selection.setSelection(binding.phoneNumberET.text, binding.phoneNumberET.text!!.length)
                    }
                }
            }, object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean, extractedValue: String, formattedValue: String) {
                    phoneNumberFilled = maskFilled
                    extractedPhoneNumber = extractedValue
                }
            })

        binding.phoneNumberET.addTextChangedListener(maskedETListener)
        binding.phoneNumberET.onFocusChangeListener = maskedETListener
    }

    private fun checkPhoneNumber(){
        binding.continueBtn.setOnClickListener {
            if(phoneNumberFilled){
                openSmsCodeFragment()
            }else{
                InputUtils.showErrorMessage(requireContext(), binding.phoneNumberCL,
                    getString(R.string.enter_phone_number_fully))
            }
        }
    }
    private fun openSmsCodeFragment(){
        findNavController().navigate(
            PhoneNumberFragmentDirections.actionPhoneNumberFragment2ToSmsCodeFragment(
                binding.phoneNumberET.text.toString(), "${Config.COUNTRY_CODE}$extractedPhoneNumber")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}