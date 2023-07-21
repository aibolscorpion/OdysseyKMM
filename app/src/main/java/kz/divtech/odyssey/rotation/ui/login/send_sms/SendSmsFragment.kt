package kz.divtech.odyssey.rotation.ui.login.send_sms

import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.phone.SmsRetriever
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isFailure
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.databinding.FragmentSendSmsBinding
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.utils.KeyboardUtils.hideKeyboard
import kz.divtech.odyssey.rotation.utils.KeyboardUtils.showKeyboard
import kz.divtech.odyssey.rotation.utils.NetworkUtils.isNetworkAvailable
import java.net.UnknownHostException

class SendSmsFragment : Fragment(), OnFilledListener, SmsBroadcastReceiver.OTPReceiveListener {
    private val editTextList = ArrayList<EditText>()
    private var _dataBinding: FragmentSendSmsBinding? = null
    internal val dataBinding  get() = _dataBinding!!

    private var countDownTimer : CountDownTimer?= null
    private val viewModel: SendSmsViewModel by activityViewModels{
        SendSmsViewModel.FillCodeViewModelFactory(
            (activity as MainActivity).employeeRepository,
            (activity?.application as App).loginRepository
        )
    }
    private var smsReceiver: SmsBroadcastReceiver? = null
    val args: SendSmsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _dataBinding = FragmentSendSmsBinding.inflate(inflater)
        dataBinding.codeFragment = this
        dataBinding.descriptionTV.text =
            requireContext().getString(R.string.code_was_sent_to_number, args.phoneNumber)

        setupEditTexts()

        dataBinding.toolBar.setOnClickListener { backToPhoneNumberFragment()}

        smsReceiver = SmsBroadcastReceiver()
        smsReceiver?.setListener(this)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.viewModel = viewModel

        viewModel.smsCodeResult.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { response ->
                if(response.isSuccess()) {
                    viewModel.setAuthLogId(response.asSuccess().value.auth_log_id)
                    startTimer(Config.COUNT_DOWN_TIMER_SECONDS)
                    showKeyboard(requireContext(), editTextList[0])
                }else if(response.isHttpException() && (response.statusCode == Constants.TOO_MANY_REQUEST_CODE)){
                    val seconds = Integer.valueOf(response.headers?.get(Constants.RETRY_AFTER)!!)
                    showContactSupportBtn()
                    startTimer(seconds)
                    showErrorMessage(requireContext(), dataBinding.sendSmsFL,
                        getString(R.string.too_many_request_message, seconds))
                }else if(response.isFailure() && response.error !is UnknownHostException){
                    showErrorMessage(requireContext(), dataBinding.sendSmsFL, "$response")
                }
                editTextList.isEnabled(true)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { response ->
                if(response.isSuccess()) {
                    openMainActivity()
                }else if(response.isHttpException() && (response.statusCode == Constants.TOO_MANY_REQUEST_CODE)) {
                    val seconds = Integer.valueOf(response.headers?.get(Constants.RETRY_AFTER)!!)
                    showErrorMessage(requireContext(), dataBinding.sendSmsFL, getString(R.string.too_many_request_message, seconds))
                }else{
                    showErrorMessage(requireContext(), dataBinding.sendSmsFL, "$response")
                }
            }
        }

        if(requireContext().isNetworkAvailable()){
            viewModel.requestSmsCode(args.extractedPhoneNumber)
        }else{
            showNoInternetDialog()
        }

        SmsRetriever.getClient(requireActivity()).startSmsRetriever()
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
        editTextList.isEnabled(false)
    }

    private fun ArrayList<EditText>.isEnabled(enable: Boolean){
        this.forEach{
            it.isEnabled = enable
        }
    }

    private fun startTimer(time: Int) {
        countDownTimer?.cancel()
        val longTime = time * 1000L
        dataBinding.timerTextView.visibility = View.VISIBLE
        dataBinding.resendSmsBtn.visibility = View.INVISIBLE
        countDownTimer = object: CountDownTimer(longTime, Config.COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                dataBinding.timerTextView.text = requireContext().
                    getString(R.string.resend_sms_with_seconds, millisUntilFinished/1000)
            }

            override fun onFinish() {
                dataBinding.timerTextView.visibility = View.GONE
                dataBinding.resendSmsBtn.visibility = View.VISIBLE
                this@SendSmsFragment.hideContactSupportBtn()
            }
        }.start()
    }


    fun sendSmsCodeAgain() {
        if(requireContext().isNetworkAvailable()){
            viewModel.requestSmsCode(args.extractedPhoneNumber)
        }else{
            showNoInternetDialog()
        }
    }

    private fun showContactSupportBtn(){
        dataBinding.contactSupportLLC.visibility = View.VISIBLE
    }

    fun hideContactSupportBtn(){
        dataBinding.contactSupportLLC.visibility = View.GONE
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

    private fun ifOneOfEditTextIsEmpty(): Boolean{
        editTextList.forEach { editText ->
            if(editText.text.isEmpty())
                return true
        }
        return false
    }

    override fun onFilled() {
        if(ifOneOfEditTextIsEmpty()){
            showErrorMessage(requireContext(), dataBinding.sendSmsFL, getString(R.string.fill_all_empty_fields))
        }else{
            hideKeyboard(requireContext(), editTextList[editTextList.size-1])
            val code = StringBuilder()
            editTextList.forEach { editText ->
                code.append(editText.text)
            }

            if(requireContext().isNetworkAvailable()){
                viewModel.login(args.extractedPhoneNumber, code.toString())
            }else{
                showNoInternetDialog()
            }
        }
    }

    fun showContactSupportDialog() = findNavController().navigate(
        R.id.action_global_contactSupportDialog)

    private fun openMainActivity(){
        findNavController().navigate(SendSmsFragmentDirections.actionGlobalMainFragment())
    }

    override fun onOTPReceived(code: String?) {
        code?.forEachIndexed { index, c ->
            editTextList[index].setText(c.toString())
        }
    }

    override fun onTimeout() {
        showErrorMessage(requireContext(), dataBinding.sendSmsFL, getString(R.string.sms_retrieve_broadcast_receiver_timeout))
    }

    private fun showNoInternetDialog(){
        findNavController().navigate(SendSmsFragmentDirections.actionGlobalNoInternetDialog())
    }
    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
        countDownTimer?.cancel()
        smsReceiver = null
    }
}