package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.app.DatePickerDialog
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.DAY_MONTH_YEAR_PATTERN
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.SERVER_DATE_PATTERN
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.formatDateToGivenPattern
import kz.divtech.odyssey.rotation.common.utils.Utils.getCountryList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DocumentBindingAdapter {

    @BindingAdapter("gender", "maleButton", "femaleButton")
    @JvmStatic fun setGender(radioGroup: RadioGroup, gender: String?, maleButton: RadioButton, femaleButton: RadioButton) {
        gender?.let {
            when(gender){
                Constants.MALE -> changeRadioButtonBackground(maleButton, femaleButton)
                Constants.FEMALE -> changeRadioButtonBackground(femaleButton, maleButton)
            }
        }
    }

    @InverseBindingAdapter(attribute = "gender")
    @JvmStatic fun getGender(radioGroup: RadioGroup): String? {
        var gender: String?= null
        when(radioGroup.checkedRadioButtonId){
            R.id.maleRadioButton -> gender = Constants.MALE
            R.id.femaleRadioButton -> gender = Constants.FEMALE
        }
        return gender
    }

    @BindingAdapter("genderAttrChanged")
    @JvmStatic fun setGenderListener(radioGroup: RadioGroup, attrChange: InverseBindingListener) {
        val maleButton = (radioGroup.getChildAt(0) as RadioButton)
        val femaleButton = (radioGroup.getChildAt(1) as RadioButton)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.maleRadioButton ->  {
                    changeRadioButtonBackground(maleButton, femaleButton)
                    attrChange.onChange()
                }
                R.id.femaleRadioButton -> {
                    changeRadioButtonBackground(femaleButton, maleButton)
                    attrChange.onChange()
                }
            }
        }
    }

    private fun changeRadioButtonBackground(checkedRadioButton: RadioButton, uncheckedRadioButton: RadioButton){
        checkedRadioButton.background = AppCompatResources.getDrawable(App.appContext, R.drawable.bg_checked_radio_button)
        checkedRadioButton.setTextColor(ContextCompat.getColor(App.appContext, R.color.white))
        uncheckedRadioButton.background = AppCompatResources.getDrawable(App.appContext, R.drawable.bg_unchecked_radio_button)
        uncheckedRadioButton.setTextColor(ContextCompat.getColor(App.appContext, R.color.profile_menu_text))
    }

    @BindingAdapter("birthDate")
    @JvmStatic fun setBirthDate(editText: EditText, date: String?){
        var formattedDate = ""
        date?.let {
            formattedDate = date.formatDateToGivenPattern(DAY_MONTH_YEAR_PATTERN)
        }
        editText.setText(formattedDate)
    }

    @InverseBindingAdapter(attribute = "birthDate")
    @JvmStatic fun getBirthDate(editText: EditText): String {
        return editText.text.toString().convertDateToServerPattern()
    }

    @BindingAdapter("birthDateAttrChanged")
    @JvmStatic fun setBirthDateListener(editText: EditText, attrChange: InverseBindingListener) {
        val todayDate = LocalDate.now()
        editText.setOnClickListener{
            DatePickerDialog(editText.context, { _, year, month, dayOfMonth ->
                val localDate = LocalDate.of(year, month+1, dayOfMonth)
                val dateFormatter = DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_PATTERN)
                editText.setText(localDate.format(dateFormatter))
                attrChange.onChange()
            }, todayDate.year, todayDate.monthValue-1, todayDate.dayOfMonth).show()
        }
    }


    private fun String?.convertDateToServerPattern(): String{
        var formattedDate = ""
        this?.let {
            val serverDateTimeFormat = DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_PATTERN)
            val parsedDateTime = LocalDate.parse(this, serverDateTimeFormat)

            val format = DateTimeFormatter.ofPattern(SERVER_DATE_PATTERN)
            formattedDate = parsedDateTime.format(format)
        }
        return formattedDate
    }

    @BindingAdapter("countryCode")
    @JvmStatic fun setCountryNameByCode(view: View, countryCode: String?) {
        countryCode?.let{
            getCountryList().forEachIndexed { _, country ->
                if (country.code == it) {
                    when (view) {
                        is EditText -> view.setText(country.name)
                        is TextView -> view.text = country.name
                        else -> throw IllegalArgumentException("Unsupported view type: ${view.javaClass.simpleName}")
                    }
                }
            }
        }
    }


}