package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.DAY_MONTH_YEAR_PATTERN
import kz.divtech.odyssey.rotation.common.utils.LocalDateTimeUtils.formatDateToGivenPattern

object DocumentBindingAdapter {
    @BindingAdapter("documentType")
    @JvmStatic fun setDocumentName(textView: TextView, documentType: String?){
        val context = textView.context
         val documentNames = mutableMapOf(
            Constants.ID_CARD to context.getString(R.string.id),
            Constants.PASSPORT to context.getString(R.string.passport),
            Constants.RESIDENCE to context.getString(R.string.residency_permit),
            Constants.FOREIGN to context.getString(R.string.foreign_document))

        textView.text = documentNames[documentType]
    }

    @BindingAdapter("issueDate","expireDate")
    @JvmStatic fun setIssueExpireDate(textView: TextView, issueDate: String?, expireDate: String?){
        val context = textView.context
        val formattedIssueDate = issueDate.formatDateToGivenPattern(DAY_MONTH_YEAR_PATTERN)
        val formattedExpireDate = expireDate.formatDateToGivenPattern(DAY_MONTH_YEAR_PATTERN)

        textView.text = context.getString(R.string.dash_sign_btw_two_text,
            formattedIssueDate, formattedExpireDate)
    }
}