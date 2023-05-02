package kz.divtech.odyssey.rotation.ui.login.send_sms

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kz.divtech.odyssey.rotation.R

class GenericTextWatcher(private val currentView: EditText, private val nextView: EditText?,
                         private val listener : OnFilledListener?) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        val text = s.toString()
        when (currentView.id) {
            R.id.digitOneET -> if (text.length == 1) nextView?.requestFocus()
            R.id.digitTwoET -> if (text.length == 1) nextView?.requestFocus()
            R.id.digitThreeET -> if (text.length == 1) nextView?.requestFocus()
            R.id.digitFourET -> if(text.length == 1) listener?.onFilled()
        }
    }
}