package kz.divtech.odyssey.rotation.ui.help.questions_answers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class QuestionsAnswers(val title: String, val content: String) : Parcelable