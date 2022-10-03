package kz.divtech.odyssey.rotation.ui.login.code

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.Config
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentCodeBinding
import kz.divtech.odyssey.rotation.utils.Utils.showKeyboard

class CodeFragment : Fragment() {
    lateinit var digitOneET : EditText
    private var dataBinding : FragmentCodeBinding? = null
    private var countDownTimer : CountDownTimer?= null
    private var countSmsBtnPress  = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        dataBinding = FragmentCodeBinding.inflate(inflater)
        dataBinding!!.codeFragment = this
        val view = dataBinding!!.root

        val args = CodeFragmentArgs.fromBundle(requireArguments())
        dataBinding!!.phoneNumber = args.phoneNumber

        digitOneET = view.findViewById(R.id.digitOneET)

        view.findViewById<View>(R.id.toolBar).setOnClickListener {
            backToPhoneNumberFragment()}

        startTimer()

        return view
    }

    private fun startTimer(){
        countDownTimer = object : CountDownTimer(Config.COUNT_DOWN_TIMER_SECONDS, Config.COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                dataBinding!!.seconds = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
                openIINFragment()
                if(countSmsBtnPress == Config.SMS_RECEIVE_ATTEMPT){
                    showContactSupportBtn()
                    return
                }
                dataBinding!!.resendSmsTimerTextView.visibility = View.GONE
                dataBinding!!.loginBtn.visibility = View.VISIBLE
            }

        }.start()
    }

    fun openIINFragment(){
        val action = CodeFragmentDirections.actionCodeFragmentToIINFragment()
        findNavController().navigate(action)
    }

    fun showTimer(){
        countSmsBtnPress+=1
        dataBinding?.loginBtn!!.visibility = View.GONE
        dataBinding?.resendSmsTimerTextView!!.visibility = View.VISIBLE
        startTimer()
    }

    fun showContactSupportBtn(){
        dataBinding?.resendSmsTimerTextView!!.visibility = View.GONE
        dataBinding?.loginBtn!!.visibility = View.GONE
        dataBinding?.contactSupportBtn!!.visibility = View.VISIBLE

    }

    override fun onStart() {
        super.onStart()

        showKeyboard(requireContext(), digitOneET)
    }

    fun backToPhoneNumberFragment(){
        countDownTimer?.cancel()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding = null
    }
}