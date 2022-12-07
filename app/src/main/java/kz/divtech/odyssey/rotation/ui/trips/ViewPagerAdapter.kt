package kz.divtech.odyssey.rotation.ui.trips

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) :  FragmentStateAdapter(fragment) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun createFragment(position: Int): Fragment =  mFragmentList[position]

    override fun getItemCount(): Int = mFragmentList.size

    fun addFragment(fragment: Fragment, title : String){
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    fun getPageTitle(position : Int) : String = mFragmentTitleList[position]

}