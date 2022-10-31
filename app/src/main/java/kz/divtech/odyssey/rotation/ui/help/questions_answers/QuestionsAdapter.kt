package kz.divtech.odyssey.rotation.ui.help.questions_answers

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.ItemQuestionAnswerBinding

class QuestionsAdapter(private val newsList : List<QuestionsAnswers>) : RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuestionAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentQuestionAnswer = newsList[position]
        holder.binding.questionsAnswers = currentQuestionAnswer
        holder.binding.questionTitleLLC.setOnClickListener {
            if (holder.binding.answerContentTV.isVisible) {
                holder.binding.showContentIV.setImageResource(R.drawable.icons_tabs_plus)
                holder.binding.answerContentTV.visibility = View.GONE
                TextViewCompat.setTextAppearance(
                    holder.binding.questionTitleTV,
                    R.style.unselected_question_title
                )
            } else {
                holder.binding.showContentIV.setImageResource(R.drawable.icons_tabs_close_blue)
                holder.binding.answerContentTV.visibility = VISIBLE
                TextViewCompat.setTextAppearance(
                    holder.binding.questionTitleTV,
                    R.style.selected_question_title
                )
            }
        }
    }

    override fun getItemCount() = newsList.size

    class ViewHolder(val binding: ItemQuestionAnswerBinding) : RecyclerView.ViewHolder(binding.root)
}