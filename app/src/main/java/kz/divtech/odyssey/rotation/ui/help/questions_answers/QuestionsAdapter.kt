package kz.divtech.odyssey.rotation.ui.help.questions_answers

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
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

    }

    override fun getItemCount() = newsList.size

    class ViewHolder(val binding: ItemQuestionAnswerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.questionTitleLLC.setOnClickListener {
                if(binding.answerContentTV.isVisible){
                    TransitionManager.beginDelayedTransition(binding.questionAnswersCV)
                    binding.showContentIV.setImageResource(R.drawable.icons_tabs_plus)
                    binding.answerContentTV.visibility = View.GONE
                }else{
                    TransitionManager.beginDelayedTransition(binding.questionAnswersCV)
                    binding.showContentIV.setImageResource(R.drawable.icons_tabs_close_blue)
                    binding.answerContentTV.visibility = VISIBLE

                }

            }
        }
    }
}