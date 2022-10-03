package kz.divtech.odyssey.rotation.ui.login.listener

interface DialogListener : OnCloseListener {
    fun contact()
    override fun close()
}