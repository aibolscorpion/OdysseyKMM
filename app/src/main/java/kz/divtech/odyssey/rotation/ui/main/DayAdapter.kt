package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kz.divtech.odyssey.rotation.R
import org.threeten.bp.LocalDate

class DayAdapter() : DayBinder<DayAdapter.DayViewContainer> {

    override fun create(view: View): DayViewContainer {
        return DayViewContainer(view)
    }

    override fun bind(container: DayViewContainer, day: CalendarDay) {
        container.bind(day)
    }

    inner class DayViewContainer(view: View) : ViewContainer(view){
        private val calendarDayTextView : TextView = view.findViewById(R.id.calendarDayText)

        fun bind(day : CalendarDay){
            calendarDayTextView.text = day.date.dayOfMonth.toString()
            if(day.owner == DayOwner.THIS_MONTH){
                calendarDayTextView.visibility = View.VISIBLE
                if(day.date.dayOfMonth < LocalDate.now().dayOfMonth){
                    calendarDayTextView.setTextColor(ContextCompat.getColor(view.context, R.color.past_day))
                }else if(day.date == LocalDate.now()){
                    calendarDayTextView.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                    calendarDayTextView.background = ContextCompat.getDrawable(view.context, R.drawable.bg_today_date)
                }
            }else{
                calendarDayTextView.visibility = View.INVISIBLE
            }

        }
    }
}