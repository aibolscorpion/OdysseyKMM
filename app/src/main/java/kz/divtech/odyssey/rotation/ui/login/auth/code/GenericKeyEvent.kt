package kz.divtech.odyssey.rotation.ui.login.auth.code

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import kz.divtech.odyssey.rotation.R

class GenericKeyEvent(private val currentView : EditText,private val previousView : EditText?) : View.OnKeyListener {
    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.digitOneET && currentView.text.isEmpty()){
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }
}