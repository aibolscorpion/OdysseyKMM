package kz.divtech.odyssey.rotation.ui.login.phonenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentNumberBinding
import kz.divtech.odyssey.rotation.utils.Utils.showKeyboard


class PhoneNumberFragment : Fragment() {
    private var ediTextFilled : Boolean = false
    private lateinit var phoneNumberET : EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding = FragmentNumberBinding.inflate(inflater)
        dataBinding.phoneNumberFragment = this
        val view = dataBinding.root

        phoneNumberET = view.findViewById(R.id.phoneNumberET)

        val maskedETListener = MaskedTextChangedListener(
            getString(R.string.phone_number_format),
            true, phoneNumberET, null, object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(maskFilled: Boolean,extractedValue: String,formattedValue: String) {
                    ediTextFilled = maskFilled
                }

            })

        phoneNumberET.addTextChangedListener(maskedETListener)
        phoneNumberET.onFocusChangeListener = maskedETListener

        showAccountDeactivatedDialog()
        return view
    }

    override fun onStart() {
        super.onStart()


        showKeyboard(requireContext(), phoneNumberET)
    }


    fun checkPhoneNumber(){
        if(!ediTextFilled){
            Toast.makeText(requireContext(), R.string.enter_phone_number_fully, Toast.LENGTH_SHORT).show()
            return
        }
        openCodeFragment()
    }

    private fun openCodeFragment(){
        val action = PhoneNumberFragmentDirections.actionPhoneNumberFragmentToCodeFragment(phoneNumberET.text.toString())
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