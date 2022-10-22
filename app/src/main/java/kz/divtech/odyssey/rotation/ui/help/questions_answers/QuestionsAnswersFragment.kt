package kz.divtech.odyssey.rotation.ui.help.questions_answers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentsQuestionsAnswersBinding

class QuestionsAnswersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentsQuestionsAnswersBinding.inflate(inflater)


        val listOfQuestionsAnswers = mutableListOf(
            QuestionsAnswers(getString(R.string.question_placeholder),getString(R.string.answer_placeholder)),
            QuestionsAnswers(getString(R.string.question_placeholder),getString(R.string.answer_placeholder)),
            QuestionsAnswers(getString(R.string.question_placeholder),getString(R.string.answer_placeholder)),
            QuestionsAnswers(getString(R.string.question_placeholder),getString(R.string.answer_placeholder)))
        binding.questionsAnswersRV.adapter = QuestionsAdapter(listOfQuestionsAnswers)


        activity?.findViewById<TextView>(R.id.toolbarTitleTV)?.setText(R.string.questions_and_answers)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

}