package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.DAY_MONTH_YEAR_PATTERN
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.formatByGivenPattern

object DocumentBindingAdapter {
    @BindingAdapter("documentType")
    @JvmStatic fun setDocumentName(textView: TextView, documentType: String?){
         val documentNames = mutableMapOf(
            Constants.ID_CARD to App.appContext.getString(R.string.id),
            Constants.PASSPORT to App.appContext.getString(R.string.passport),
            Constants.RESIDENCE to App.appContext.getString(R.string.residency_permit))

        textView.text = documentNames[documentType]
    }

    @BindingAdapter("issueDate","expireDate")
    @JvmStatic fun setIssueExpireDate(textView: TextView, issueDate: String?, expireDate: String?){
        val formattedIssueDate = formatByGivenPattern(issueDate, DAY_MONTH_YEAR_PATTERN)
        val formattedExpireDate = formatByGivenPattern(expireDate, DAY_MONTH_YEAR_PATTERN)

        textView.text = App.appContext.getString(R.string.dash_sign_btw_two_text,
            formattedIssueDate, formattedExpireDate)
    }
}