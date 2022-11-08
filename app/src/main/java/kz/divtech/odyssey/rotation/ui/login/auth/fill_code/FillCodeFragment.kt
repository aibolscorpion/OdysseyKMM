package kz.divtech.odyssey.rotation.ui.login.auth.fill_code

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentFillCodeBinding
import kz.divtech.odyssey.rotation.utils.Utils.hideKeyboard
import kz.divtech.odyssey.rotation.utils.Utils.showKeyboard
import kz.divtech.odyssey.rotation.ui.login.auth.AuthViewModel

class FillCodeFragment : Fragment(), OnFilledListener {
    private val editTextList = ArrayList<EditText>()
    private lateinit var dataBinding : FragmentFillCodeBinding
    private var countDownTimer : CountDownTimer?= null
    private var countSmsBtnPress  = 1
    private val viewModel by lazy { ViewModelProvider(requireActivity())[AuthViewModel::class.java] }
    private lateinit var phoneNumber : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        dataBinding = FragmentFillCodeBinding.inflate(inflater)
        dataBinding.codeFragment = this

        val args = FillCodeFragmentArgs.fromBundle(requireArguments())
        phoneNumber = args.phoneNumber
        dataBinding.phoneNumber = phoneNumber

        setupEditTexts()

        dataBinding.toolBar.setOnClickListener {
            backToPhoneNumberFragment()}

        startTimer()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.message.observe(viewLifecycleOwner){ message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.isSuccessfullyLoggedIn.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(FillCodeFragmentDirections.actionGlobalMainActivity())
            }
        }
    }

    private fun setupEditTexts(){
        editTextList.add(dataBinding.digitOneET)
        editTextList.add(dataBinding.digitTwoET)
        editTextList.add(dataBinding.digitThreeET)
        editTextList.add(dataBinding.digitFourET)

        editTextList.forEachIndexed { index, editText ->
            if(index < editTextList.size-1)
                editText.addTextChangedListener(GenericTextWatcher(editText, editTextList[index+1], null))
            else
                editText.addTextChangedListener(GenericTextWatcher(editText, null, this))

            if(index > 0)
                editText.setOnKeyListener(GenericKeyEvent(editText, editTextList[index-1]))
            else
                editText.setOnKeyListener(GenericKeyEvent(editText, null))
        }
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

    fun sendSmsCodeAgain(){
        countSmsBtnPress+=1
        dataBinding.loginBtn.visibility = View.GONE
        dataBinding.resendSmsTimerTextView.visibility = View.VISIBLE

        viewModel.sendSmsToPhone(null)
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

        showKeyboard(requireContext(), editTextList[0])
    }

    fun backToPhoneNumberFragment(){
        countDownTimer?.cancel()
        findNavController().popBackStack()
    }

    private fun checkIfAllEditTextsFilled(){
        var oneOfEditTextIsEmpty = false
        editTextList.forEach { if(it.text.isEmpty()) oneOfEditTextIsEmpty = true }

        if(oneOfEditTextIsEmpty){
            Toast.makeText(requireContext(), R.string.fill_all_empty_fields, Toast.LENGTH_SHORT).show()
            return
        }

    }
    override fun onFilled() {

        checkIfAllEditTextsFilled()

        hideKeyboard(requireContext(), editTextList[editTextList.size-1])
        val code = StringBuilder()
        editTextList.forEach {  code.append(it.text)}

        viewModel.login(code.toString())
    }

}