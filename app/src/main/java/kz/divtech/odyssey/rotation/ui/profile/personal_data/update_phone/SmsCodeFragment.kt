package kz.divtech.odyssey.rotation.ui.profile.personal_data.update_phone

import android.annotation.SuppressLint
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.databinding.FragmentEnterCodeBinding
import kz.divtech.odyssey.rotation.domain.model.errors.ValidationErrorResponse
import kz.divtech.odyssey.rotation.ui.login.send_sms.GenericKeyEvent
import kz.divtech.odyssey.rotation.ui.login.send_sms.GenericTextWatcher
import kz.divtech.odyssey.rotation.ui.login.send_sms.OnFilledListener
import kz.divtech.odyssey.rotation.ui.login.send_sms.SmsBroadcastReceiver
import kz.divtech.odyssey.rotation.common.utils.InputUtils
import kz.divtech.odyssey.rotation.common.utils.KeyboardUtils
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable

@AndroidEntryPoint
class SmsCodeFragment: Fragment(), OnFilledListener, SmsBroadcastReceiver.OTPReceiveListener  {
    private var _dataBinding: FragmentEnterCodeBinding? = null
    internal val dataBinding get() = _dataBinding!!
    val args: SmsCodeFragmentArgs by navArgs()
    val viewModel: UpdatePhoneViewModel by viewModels()
    private val editTextList = ArrayList<EditText>()
    private var countDownTimer : CountDownTimer?= null
    private var smsReceiver: SmsBroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataBinding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.viewModel = viewModel
        setupEditTexts()
        smsReceiver = SmsBroadcastReceiver()
        smsReceiver?.setListener(this)

        if(requireContext().isNetworkAvailable()){
            viewModel.requestSmsCode(args.extractedPhoneNumber)
        }else{
            showNoInternetDialog()
        }

        dataBinding.smsCodeDescTV.text = requireContext().
            getString(R.string.enter_code_that_was_sent_to_number, args.phoneNumber)
        dataBinding.resendSmsBtn.setOnClickListener {
            if(requireContext().isNetworkAvailable()){
                viewModel.requestSmsCode(args.extractedPhoneNumber)
            }else{
                showNoInternetDialog()
            }
        }

        viewModel.smsCodeResult.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { response ->
                if(response.isSuccess()) {
                    viewModel.setAuthLogId(response.asSuccess().value.authLogId)
                    startTimer(Config.COUNT_DOWN_TIMER_SECONDS)
                    editTextList.isEnabled(true)
                    KeyboardUtils.showSoftKeyboard(requireContext(), editTextList[0])
                }else if(response.isHttpException() && (response.statusCode == Constants.TOO_MANY_REQUEST_CODE)){
                    val seconds = Integer.valueOf(response.headers?.get(Constants.RETRY_AFTER)!!)
                    startTimer(seconds)
                    InputUtils.showErrorMessage(requireContext(), dataBinding.sendSmsFL,
                        getString(R.string.too_many_request_message, seconds))
                }else if(response.isHttpException() && (response.statusCode == Constants.UNPROCESSABLE_ENTITY_CODE)){
                    val errorResponse = Gson().fromJson(response.error.errorBody?.string(),
                        ValidationErrorResponse::class.java)
                    errorResponse.errors.forEach{ (field, errorMessages) ->
                        val firstErrorMessage = errorMessages.first()
                        if(field == "phone"){
                            InputUtils.showErrorMessage(requireContext(), dataBinding.sendSmsFL, firstErrorMessage)
                        }
                    }
                }else{
                    InputUtils.showErrorMessage(requireContext(), dataBinding.sendSmsFL, "$response")
                }
            }
        }

        viewModel.updatedResult.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { response ->
                if(response.isSuccess()) {
                    openPhoneUpdatedFragment()
                }else if(response.isHttpException() && (response.statusCode == Constants.TOO_MANY_REQUEST_CODE)) {
                    val seconds = Integer.valueOf(response.headers?.get(Constants.RETRY_AFTER)!!)
                    InputUtils.showErrorMessage(
                        requireContext(), dataBinding.sendSmsFL,
                        getString(R.string.too_many_request_message, seconds))
                }else{
                    InputUtils.showErrorMessage(requireContext(), dataBinding.sendSmsFL,
                        "$response")
                }
            }
        }

        SmsRetriever.getClient(requireActivity()).startSmsRetriever()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context?.registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
                RECEIVER_NOT_EXPORTED
            )
        }else{
            context?.registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        }
    }

    override fun onPause() {
        super.onPause()

        activity?.unregisterReceiver(smsReceiver)
    }

    private fun startTimer(time: Int){
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
            }
        }.start()
    }

    private fun ArrayList<EditText>.isEnabled(enable: Boolean){
        this.forEach{
            it.isEnabled = enable
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

    private fun ifOneOfEditTextIsEmpty(): Boolean{
        editTextList.forEach { editText ->
            if(editText.text.isEmpty())
                return true
        }
        return false
    }

    override fun onFilled() {
        if(ifOneOfEditTextIsEmpty()){
            InputUtils.showErrorMessage(requireContext(), dataBinding.enterCodeCL,
                getString(R.string.fill_all_empty_fields))
        }else{
            KeyboardUtils.hideSoftKeyboard(requireContext(), editTextList[editTextList.size - 1])
            val code = StringBuilder()
            editTextList.forEach { editText ->
                code.append(editText.text)
            }

            if(requireContext().isNetworkAvailable()){
                viewModel.confirmUpdate(code.toString())
            }else{
                showNoInternetDialog()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
        countDownTimer?.cancel()
        smsReceiver = null
    }

    override fun onOTPReceived(code: String?) {
        code?.forEachIndexed { index, c ->
            editTextList[index].setText(c.toString())
        }
    }

    override fun onTimeout() {
        InputUtils.showErrorMessage(requireContext(), dataBinding.enterCodeCL,
            getString(R.string.sms_retrieve_broadcast_receiver_timeout))
    }

    private fun showNoInternetDialog(){
        findNavController().navigate(SmsCodeFragmentDirections.actionGlobalNoInternetDialog())
    }

    private fun openPhoneUpdatedFragment(){
        findNavController().navigate(
            SmsCodeFragmentDirections.actionSmsCodeFragmentToPhoneUpdatedFragment(args.phoneNumber))
    }

}