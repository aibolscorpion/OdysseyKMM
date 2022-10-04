package kz.divtech.odyssey.rotation.ui.login.code

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentCodeBinding
import kz.divtech.odyssey.rotation.utils.Utils.hideKeyboard
import kz.divtech.odyssey.rotation.utils.Utils.showKeyboard

class CodeFragment : Fragment(), OnFilledListener {
    private lateinit var digitOneET : EditText
    private lateinit var digitTwoET : EditText
    private lateinit var digitThreeET : EditText
    private lateinit var digitFourET : EditText
    private lateinit var dataBinding : FragmentCodeBinding
    private var countDownTimer : CountDownTimer?= null
    private var countSmsBtnPress  = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        dataBinding = FragmentCodeBinding.inflate(inflater)
        dataBinding.codeFragment = this

        val args = CodeFragmentArgs.fromBundle(requireArguments())
        dataBinding.phoneNumber = args.phoneNumber

        setEditTexts()

        dataBinding.toolBar.setOnClickListener {
            backToPhoneNumberFragment()}

        startTimer()

        return dataBinding.root
    }

    private fun setEditTexts(){
        digitOneET = dataBinding.digitOneET
        digitTwoET = dataBinding.digitTwoET
        digitThreeET = dataBinding.digitThreeET
        digitFourET = dataBinding.digitFourET

        digitOneET.addTextChangedListener(GenericTextWatcher(digitOneET, digitTwoET, null))
        digitTwoET.addTextChangedListener(GenericTextWatcher(digitTwoET, digitThreeET, null))
        digitThreeET.addTextChangedListener(GenericTextWatcher(digitThreeET, digitFourET, null))
        digitFourET.addTextChangedListener(GenericTextWatcher(digitFourET, null, this))

        digitOneET.setOnKeyListener(GenericKeyEvent(digitOneET, null))
        digitTwoET.setOnKeyListener(GenericKeyEvent(digitTwoET, digitOneET))
        digitThreeET.setOnKeyListener(GenericKeyEvent(digitThreeET, digitTwoET))
        digitFourET.setOnKeyListener(GenericKeyEvent(digitFourET,digitThreeET))
    }

    private fun startTimer(){
        countDownTimer = object : CountDownTimer(Config.COUNT_DOWN_TIMER_SECONDS, Config.COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                dataBinding.seconds = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
//                openIINFragment()
                if(countSmsBtnPress == Config.SMS_RECEIVE_ATTEMPT){
                    showContactSupportBtn()
                    return
                }
                dataBinding.resendSmsTimerTextView.visibility = View.GONE
                dataBinding.loginBtn.visibility = View.VISIBLE
            }

        }.start()
    }

    fun openIINFragment(){
        val action = CodeFragmentDirections.actionCodeFragmentToIINFragment()
        findNavController().navigate(action)
    }

    fun showTimer(){
        countSmsBtnPress+=1
        dataBinding.loginBtn.visibility = View.GONE
        dataBinding.resendSmsTimerTextView.visibility = View.VISIBLE
        startTimer()
    }

    fun showContactSupportBtn(){
        dataBinding.resendSmsTimerTextView.visibility = View.GONE
        dataBinding.loginBtn.visibility = View.GONE
        dataBinding.contactSupportTV.visibility = View.VISIBLE
        dataBinding.contactSupportBtn.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()

        showKeyboard(requireContext(), digitOneET)
    }

    fun backToPhoneNumberFragment(){
        countDownTimer?.cancel()
        findNavController().popBackStack()
    }

    override fun onFilled() {
        if(digitOneET.text.isNotEmpty() && digitTwoET.text.isNotEmpty()
            && digitThreeET.text.isNotEmpty() && digitFourET.text.isNotEmpty()){
            hideKeyboard(requireContext(), digitFourET)
            val code = StringBuilder().append(digitOneET.text).append(digitTwoET.text).append(digitThreeET.text).append(digitFourET.text)
            Toast.makeText(requireContext(), code, Toast.LENGTH_SHORT).show()
            openIINFragment()
        }else{
            Toast.makeText(requireContext(), R.string.fill_all_empty_fields, Toast.LENGTH_SHORT).show()
            return
        }
    }

}