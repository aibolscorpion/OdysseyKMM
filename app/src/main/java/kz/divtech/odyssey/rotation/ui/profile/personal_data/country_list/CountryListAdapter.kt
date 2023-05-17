package kz.divtech.odyssey.rotation.ui.profile.personal_data.country_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemCountryBinding
import kz.divtech.odyssey.rotation.databinding.ItemCountryHeaderBinding
import kz.divtech.odyssey.rotation.domain.model.profile.Country

class CountryListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val countryList = mutableListOf<Country>()
    var checkedPosition: Int? = null
    private var oldCheckedPosition: Int? = null
    var country: Country? = null

    private val viewTypePopularHeader = 0
    private val viewTypeAllHeader = 1
    private val viewTypeCountryItem = 2

    override fun getItemViewType(position: Int): Int {
        val country = countryList[position]
        return when {
            country.code == "POP" && position == 0  -> viewTypePopularHeader
            country.code == "ALL" && position == 6  -> viewTypeAllHeader
            else -> viewTypeCountryItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            viewTypePopularHeader, viewTypeAllHeader -> {
                val binding = ItemCountryHeaderBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false)
                CountryHeaderViewHolder(binding)
            }
            viewTypeCountryItem -> {
                val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false)
                CountryViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CountryViewHolder -> {
                holder.binding.countryItemRB.isChecked = checkedPosition == position
                holder.binding.countryItemTV.text = countryList[position].name
            }
            is CountryHeaderViewHolder -> {
                holder.binding.headerTV.text = countryList[position].name
            }
        }
    }

    override fun getItemCount(): Int = countryList.size

    fun setCountryList(newCountryList: List<Country>, countryCode: String){
        countryList.clear()
        countryList.addAll(newCountryList)
        checkedPosition = getPositionByCountryCode(countryCode)
        country = checkedPosition?.let { countryList[it] }
        notifyDataSetChanged()
    }


    private fun getPositionByCountryCode(countryCode: String): Int?{
        countryList.forEachIndexed { pos, it ->
            if(it.code == countryCode){
                return pos
            }
        }
        return null
    }

    inner class CountryViewHolder(val binding: ItemCountryBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.countryItemLLC.setOnClickListener {
                oldCheckedPosition = checkedPosition
                checkedPosition = bindingAdapterPosition
                country = checkedPosition?.let { countryList[it] }
                oldCheckedPosition?.let { notifyItemChanged(it) }
                checkedPosition?.let { notifyItemChanged(it) }
            }
        }
    }

    inner class CountryHeaderViewHolder(val binding: ItemCountryHeaderBinding):
        RecyclerView.ViewHolder(binding.root)

}