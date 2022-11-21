package kz.divtech.odyssey.rotation.ui.trips

import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App

enum class ApplicationStatus(private val descriptionResource: Int) {
    OPENED_WITHOUT_DETAILS(R.string.opened_with_details_desc),
    OPENED_WITH_DETAILS(R.string.opened_without_details_desc),
    OPENED_ON_THE_WAITING_LIST(R.string.opened_on_the_waiting_list_desc),
    OPENED_WITH_DETAILS_AND_OPENED_ON_THE_WAITING_LIST(R.string.opened_with_details_and_opened_on_the_waiting_list_desc),
    PARTLY_ISSUED_AND_OPENED(R.string.partly_issued_and_opened_desc),
    PARTLY_ISSUED_AND_OPENED_ON_THE_WAITING_LIST(R.string.partly_issued_and_opened_on_the_waiting_list_desc),
    RETURNED_FULLY(R.string.returned_fully_desc),
    RETURNED_PARTLY(R.string.returned_partly_desc),
    ISSUED(R.string.issued_desc);

    fun getDescription(): String{
        return App.appContext.getString(descriptionResource)
    }
    

}