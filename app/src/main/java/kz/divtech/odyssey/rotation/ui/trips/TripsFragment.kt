package kz.divtech.odyssey.rotation.ui.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentTripsBinding
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.ActiveTripsFragment


class TripsFragment : Fragment() {
    private var _binding : FragmentTripsBinding? = null
    internal val binding get() = _binding!!
    private var indicatorWidth = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentTripsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabLayout.post {
            indicatorWidth = binding.tabLayout.width / binding.tabLayout.tabCount

            val indicatorParams = binding.indicator.layoutParams as FrameLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            binding.indicator.layoutParams = indicatorParams
        }
        setupViewPager()
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.trips))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "TripsFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    private fun setupViewPager(){
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(ActiveTripsFragment.newInstance(true), getString(R.string.active_trips))
        adapter.addFragment(ActiveTripsFragment.newInstance(false), getString(R.string.archive_trips))

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = adapter.getPageTitle(position)
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val params = binding.indicator.layoutParams as FrameLayout.LayoutParams
                val translationOffset = (positionOffset+position) * indicatorWidth
                params.leftMargin = translationOffset.toInt()
                binding.indicator.layoutParams = params
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}