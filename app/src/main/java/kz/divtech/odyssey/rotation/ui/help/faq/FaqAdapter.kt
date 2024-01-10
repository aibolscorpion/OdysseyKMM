package kz.divtech.odyssey.rotation.ui.help.faq

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.ItemFaqBinding
import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

class FaqAdapter : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {
    private val oldFaqList = mutableListOf<Faq>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFaq = oldFaqList[position]
        holder.binding.faq = currentFaq
        holder.binding.faqLL.setOnClickListener {
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

    override fun getItemCount() = oldFaqList.size

    fun setList(newFaqList: List<Faq>){
        val diffCallBack = FaqDiffCallBack(newFaqList, oldFaqList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        oldFaqList.clear()
        oldFaqList.addAll(newFaqList)
        diffResult.dispatchUpdatesTo(this)
    }



    class ViewHolder(val binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root)
}