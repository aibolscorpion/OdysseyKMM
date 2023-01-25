package kz.divtech.odyssey.rotation.utils

object Utils {

    fun StringBuilder.appendWithoutNull(text: String?): StringBuilder{
        if(text != null){
            append(text)
        }
        return this
    }
}