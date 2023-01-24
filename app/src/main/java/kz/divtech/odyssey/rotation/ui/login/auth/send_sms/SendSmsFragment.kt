package kz.divtech.odyssey.rotation.ui.login.auth.send_sms

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
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentSendSmsBinding
import kz.divtech.odyssey.rotation.ui.login.auth.SmsBroadcastReceiver
import kz.divtech.odyssey.rotation.utils.KeyboardUtils.hideKeyboard
import kz.divtech.odyssey.rotation.utils.KeyboardUtils.showKeyboard
import kz.divtech.odyssey.rotation.utils.Utils.showErrorMessage

class SendSmsFragment : Fragment(), OnFilledListener, SmsBroadcastReceiver.OTPReceiveListener {
    private val editTextList = ArrayList<EditText>()
    private lateinit var dataBinding : FragmentSendSmsBinding
    private var gCountDownTimber : CountDownTimer?= null
    private lateinit var viewModel: SendSmsViewModel
    private lateinit var phoneNumber : String
    private lateinit var extractedPhoneNumber: String
    private lateinit var smsReceiver: SmsBroadcastReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        dataBinding = FragmentSendSmsBinding.inflate(inflater)
        dataBinding.codeFragment = this

        val args = SendSmsFragmentArgs.fromBundle(requireArguments())
        phoneNumber = args.phoneNumber
        extractedPhoneNumber = args.extracedPhoneNumber

        dataBinding.phoneNumber = phoneNumber

        setupEditTexts()

        dataBinding.toolBar.setOnClickListener {
            backToPhoneNumberFragment()}

        smsReceiver = SmsBroadcastReceiver()
        smsReceiver.setListener(this)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val factory = SendSmsViewModel.FillCodeViewModelFactory(
            (requireActivity().application as App).employeeRepository)
        viewModel = ViewModelProvider(requireActivity(), factory)[SendSmsViewModel::class.java]

        dataBinding.viewModel = viewModel
        viewModel.requestSmsCode(extractedPhoneNumber)

        viewModel.secondsLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { seconds ->
                showContactSupportBtn()
                startTimer(seconds)
                showErrorMessage(requireContext(), dataBinding.sendSmsFL,
                    getString(R.string.too_many_request_message, seconds)
                )
            }
        }

        viewModel.smsCodeSent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { smsCodeSent ->
                if (smsCodeSent)
                    startTimer(Config.COUNT_DOWN_TIMER_SECONDS)
            }
        }

        viewModel.loggedIn.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { loggedIn ->
                if (loggedIn) {
                    openMainActivity()
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {  event ->
            event.getContentIfNotHandled()?.let { message ->
                showErrorMessage(requireContext(), dataBinding.sendSmsFL, message)
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

    private fun startTimer(time: Int){
        val longTime = time * 1000L
        dataBinding.timerTextView.visibility = View.VISIBLE
        dataBinding.loginBtn.visibility = View.INVISIBLE
        gCountDownTimber = object: CountDownTimer(longTime, Config.COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                dataBinding.seconds = millisUntilFinished/1000
            }

            override fun onFinish() {
                dataBinding.timerTextView.visibility = View.GONE
                dataBinding.loginBtn.visibility = View.VISIBLE
                this@SendSmsFragment.hideContactSupportBtn()
            }
        }.start()
    }


    fun sendSmsCodeAgain() = viewModel.requestSmsCode(extractedPhoneNumber)

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
        gCountDownTimber?.cancel()
        findNavController().popBackStack()
    }

    private fun checkIfAllEditTextsFilled(){
        var oneOfEditTextIsEmpty = false
        editTextList.forEach { editText ->
            if(editText.text.isEmpty()) oneOfEditTextIsEmpty = true }

        if(oneOfEditTextIsEmpty){
            showErrorMessage(requireContext(), dataBinding.sendSmsFL, getString(R.string.fill_all_empty_fields))
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

        viewModel.login(extractedPhoneNumber, code.toString())
    }

    fun showContactSupportDialog() = findNavController().navigate(
        SendSmsFragmentDirections.actionGlobalContactSupportDialog())

    private fun openMainActivity(){
        findNavController().navigate(SendSmsFragmentDirections.actionGlobalMainActivity())
        (activity as AppCompatActivity).finish()
    }

    override fun onOTPReceived(code: String?) {
        code?.forEachIndexed { index, c ->
            editTextList[index].setText(c.toString())
        }
    }

    override fun onTimeout() {
        showErrorMessage(requireContext(), dataBinding.sendSmsFL, getString(R.string.sms_retrieve_broadcast_receiver_timeout))
    }

}