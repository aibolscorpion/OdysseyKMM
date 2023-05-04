package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.app.DatePickerDialog
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.DAY_MONTH_YEAR_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.formatDateToGivenPattern
import java.time.LocalDate

object DocumentBindingAdapter {

    @BindingAdapter("gender", "maleButton", "femaleButton")
    @JvmStatic fun setGender(radioGroup: RadioGroup, gender: String, maleButton: RadioButton, femaleButton: RadioButton) {
        when(gender){
            Constants.MALE -> changeRadioButtonBackground(maleButton, femaleButton)
            Constants.FEMALE -> changeRadioButtonBackground(femaleButton, maleButton)
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
        return editText.text.toString()
    }

    @BindingAdapter("birthDateAttrChanged")
    @JvmStatic fun setBirthDateListener(editText: EditText, attrChange: InverseBindingListener) {
        val todayDate = LocalDate.now()
        editText.setOnClickListener{
            DatePickerDialog(editText.context, { _, year, month, dayOfMonth ->
                val birthDate = App.appContext.getString(R.string.date_of_birth_without_values, dayOfMonth, month+1, year)
                editText.setText(birthDate)
                attrChange.onChange()
            }, todayDate.year, todayDate.monthValue-1, todayDate.dayOfMonth).show()
        }
    }

    @BindingAdapter("issueDate")
    @JvmStatic fun setIssueDate(editText: EditText, date: String?){
        if(editText.text.toString() != date){
            val formattedDate = date.formatDateToGivenPattern(DAY_MONTH_YEAR_PATTERN)
            editText.setText(formattedDate)
        }
    }

    @InverseBindingAdapter(attribute = "issueDate")
    @JvmStatic fun getIssueDate(editText: EditText): String {
        return editText.text.toString()
    }

    @BindingAdapter("issueDateAttrChanged")
    @JvmStatic fun setIssueDateListener(editText: EditText, attrChange: InverseBindingListener) {
        val todayDate = LocalDate.now()
        editText.setOnClickListener{
            DatePickerDialog(editText.context, { _, year, month, dayOfMonth ->
                val issueDate = App.appContext.getString(R.string.date_of_birth_without_values, dayOfMonth, month+1, year)
                editText.setText(issueDate)
                attrChange.onChange()
            }, todayDate.year, todayDate.monthValue-1, todayDate.dayOfMonth).show()

        }
    }

}