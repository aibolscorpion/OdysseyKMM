package kz.divtech.odyssey.rotation.ui.help.faq

import androidx.recyclerview.widget.DiffUtil
import kz.divtech.odyssey.rotation.domain.model.help.faq.Faq

class FaqDiffCallBack(private val newList: List<Faq>, private val  oldList: List<Faq>)  : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition].id == oldList[oldItemPosition].id


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition] == oldList[oldItemPosition]

}