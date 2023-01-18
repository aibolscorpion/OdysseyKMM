package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.utils.Utils

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
        val formattedIssueDate = Utils.formatByGivenPattern(issueDate, Utils.DAY_MONTH_YEAR_PATTERN)
        val formattedExpireDate = Utils.formatByGivenPattern(expireDate, Utils.DAY_MONTH_YEAR_PATTERN)

        textView.text = App.appContext.getString(R.string.dep_arrival_station_name,
            formattedIssueDate, formattedExpireDate)
    }
}