package kz.divtech.odyssey.rotation.ui.login.send_sms

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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentSendSmsBinding
import kz.divtech.odyssey.rotation.common.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.common.utils.KeyboardUtils.hideSoftKeyboard
import kz.divtech.odyssey.rotation.common.utils.KeyboardUtils.showSoftKeyboard
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.shared.common.Resource

@AndroidEntryPoint
class SendSmsFragment : Fragment(), OnFilledListener, SmsBroadcastReceiver.OTPReceiveListener {
    private val editTextList = ArrayList<EditText>()
    private var _dataBinding: FragmentSendSmsBinding? = null
    internal val dataBinding  get() = _dataBinding!!

    private var countDownTimer : CountDownTimer?= null
    private val viewModel: SendSmsViewModel by activityViewModels()
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
                when (response) {
                    is Resource.Success -> {
                        response.data?.let {
                            viewModel.setAuthLogId(it.authLogId)
                            startTimer(Config.COUNT_DOWN_TIMER_SECONDS)
                            showSoftKeyboard(requireContext(), editTextList[0])
                        }
                    }

                    is Resource.Error.HttpException.TooManyRequest -> {
                        showContactSupportBtn()
                        startTimer(response.seconds)
                        showErrorMessage(requireContext(), dataBinding.sendSmsFL, response.message.toString())
                    }

                    else -> {
                        showErrorMessage(requireContext(), dataBinding.sendSmsFL, response.message.toString())
                    }
                }
                editTextList.isEnabled(true)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { response ->
                if(response is Resource.Success) {
                    response.data?.let {
                        val isConfirmed = it.profile.uaConfirmed
                        if (isConfirmed) { openMainActivity() }
                        else { openTermsOfAgreemntFragment() }
                    }
                }else{
                    showErrorMessage(requireContext(), dataBinding.sendSmsFL, "${response.message}")
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

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context?.registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION), RECEIVER_NOT_EXPORTED)
        }else{
            context?.registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        }

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.send_sms_code))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "SendSmsFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
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
            hideSoftKeyboard(requireContext(), editTextList[editTextList.size-1])
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

    private fun openTermsOfAgreemntFragment(){
        findNavController().navigate(SendSmsFragmentDirections.actionGlobalAuthTermsFragment())
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