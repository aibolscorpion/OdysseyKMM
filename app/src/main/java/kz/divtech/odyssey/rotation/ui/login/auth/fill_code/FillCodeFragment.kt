package kz.divtech.odyssey.rotation.ui.login.auth.fill_code

import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentFillCodeBinding
import kz.divtech.odyssey.rotation.utils.Utils.hideKeyboard
import kz.divtech.odyssey.rotation.utils.Utils.showKeyboard
import kz.divtech.odyssey.rotation.ui.login.auth.AuthSharedViewModel
import kz.divtech.odyssey.rotation.ui.login.auth.SmsBroadcastReceiver
import kz.divtech.odyssey.rotation.utils.Utils.getIntFromString
import kz.divtech.odyssey.rotation.utils.Utils.showErrorMessage

class FillCodeFragment : Fragment(), OnFilledListener, SmsBroadcastReceiver.OTPReceiveListener {
    private val editTextList = ArrayList<EditText>()
    private lateinit var dataBinding : FragmentFillCodeBinding
    private var countDownTimer : CountDownTimer?= null
    private val viewModel by lazy { ViewModelProvider(requireActivity())[AuthSharedViewModel::class.java] }
    private lateinit var phoneNumber : String
    private lateinit var smsReceiver: SmsBroadcastReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        dataBinding = FragmentFillCodeBinding.inflate(inflater)
        dataBinding.codeFragment = this

        val args = FillCodeFragmentArgs.fromBundle(requireArguments())
        phoneNumber = args.phoneNumber
        dataBinding.phoneNumber = phoneNumber

        setupEditTexts()

        dataBinding.toolBar.setOnClickListener {
            backToPhoneNumberFragment()}

        startTimer(Config.COUNT_DOWN_TIMER_SECONDS)


        smsReceiver = SmsBroadcastReceiver()
        smsReceiver.setListener(this)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.codeResponse.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { codeResponse ->
                when(codeResponse.type) {
                    Constants.TOO_MANY_REQUEST -> {
                        showContactSupportBtn()
                        startTimer(getIntFromString(codeResponse.message!!))
                    }
                }
                showErrorMessage(requireContext(), dataBinding.fillCodeFL, codeResponse.message!!)
            }
        }

        viewModel.smsCodeSent.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { smsCodeSent ->
                if(smsCodeSent)
                    startTimer(Config.COUNT_DOWN_TIMER_SECONDS)
            }
        }

        viewModel.isSuccessfullyLoggedIn.observe(viewLifecycleOwner){
            if(it)  openMainActivity()
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

    private fun startTimer(time: Int){
        val longTime = time * 1000L
        dataBinding.timerTextView.visibility = View.VISIBLE
        dataBinding.loginBtn.visibility = View.INVISIBLE
        countDownTimer = object : CountDownTimer(longTime, Config.COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                dataBinding.seconds = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
                dataBinding.timerTextView.visibility = View.GONE
                dataBinding.loginBtn.visibility = View.VISIBLE
                this@FillCodeFragment.hideContactSupportBtn()
            }
        }.start()
    }

    fun sendSmsCodeAgain() =
        viewModel.sendSmsToPhone()

    private fun showContactSupportBtn(){
        dataBinding.contactSupportLLC.visibility = View.VISIBLE
    }

    fun hideContactSupportBtn(){
        dataBinding.contactSupportLLC.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()

        showKeyboard(requireContext(), editTextList[0])
    }

    override fun onResume() {
        super.onResume()

        activity?.registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
    }

    override fun onPause() {
        super.onPause()

        activity?.unregisterReceiver(smsReceiver)
    }

    fun backToPhoneNumberFragment(){
        countDownTimer?.cancel()
        findNavController().popBackStack()
    }

    private fun checkIfAllEditTextsFilled(){
        var oneOfEditTextIsEmpty = false
        editTextList.forEach { editText ->
            if(editText.text.isEmpty()) oneOfEditTextIsEmpty = true }

        if(oneOfEditTextIsEmpty){
            showErrorMessage(requireContext(), dataBinding.fillCodeFL, getString(R.string.fill_all_empty_fields))
            return
        }

    }

    override fun onFilled() {

        checkIfAllEditTextsFilled()

        hideKeyboard(requireContext(), editTextList[editTextList.size-1])
        val code = StringBuilder()
        editTextList.forEach { editText ->
            code.append(editText.text)
        }

        viewModel.login(code.toString())
    }

    fun showContactSupportDialog() = findNavController().navigate(FillCodeFragmentDirections.actionGlobalContactSupportDialog())

    private fun openMainActivity(){
        findNavController().navigate(FillCodeFragmentDirections.actionGlobalMainActivity())
        (activity as AppCompatActivity).finish()
    }

    override fun onOTPReceived(code: String?) {
        code?.forEachIndexed { index, c ->
            editTextList[index].setText(c.toString())
        }
    }

    override fun onTimeout() {
        showErrorMessage(requireContext(), dataBinding.fillCodeFL, getString(R.string.sms_retrieve_broadcast_receiver_timeout))
    }

}