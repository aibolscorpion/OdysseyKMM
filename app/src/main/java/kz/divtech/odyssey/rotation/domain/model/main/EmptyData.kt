package kz.divtech.odyssey.rotation.domain.model.main

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App

enum class EmptyData(val icon: Drawable, val title: String, val  content: String) {
    ACTIVE_TRIPS(ContextCompat.getDrawable(App.appContext, R.drawable.icon_travel)!!,
        App.appContext.getString(R.string.empty_active_trips_title),
        App.appContext.getString(R.string.empty_active_trips_content)),

    ARCHIVE_TRIPS(ContextCompat.getDrawable(App.appContext, R.drawable.icon_travel)!!,
        App.appContext.getString(R.string.empty_archive_trips_title),
        App.appContext.getString(R.string.empty_active_trips_content)),

    FAQ(ContextCompat.getDrawable(App.appContext, R.drawable.icon_faq)!!,
        App.appContext.getString(R.string.empty_faq_title),
        App.appContext.getString(R.string.empty_faq_content)),

    NEWS(ContextCompat.getDrawable(App.appContext, R.drawable.icon_news)!!,
        App.appContext.getString(R.string.news_not_found_title),
        App.appContext.getString(R.string.news_not_found_content)),

    NOTIFICATIONS(ContextCompat.getDrawable(App.appContext, R.drawable.icon_notifications)!!,
        App.appContext.getString(R.string.notifications_not_found_title),
        App.appContext.getString(R.string.notifications_not_found_content)),

    DOCUMENTS(ContextCompat.getDrawable(App.appContext, R.drawable.icon_document)!!,
        App.appContext.getString(R.string.documents_not_found_title),
        App.appContext.getString(R.string.documents_not_found_content))

}