package kz.divtech.odyssey.rotation.ui.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentTripsBinding
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.ActiveTripsFragment

class TripsFragment : Fragment() {
    private val tripsViewModel by lazy { ViewModelProvider(requireActivity())[TripsViewModel::class.java]}
    lateinit var binding : FragmentTripsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = FragmentTripsBinding.inflate(inflater)

        binding.viewModel = tripsViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripsViewModel.getTrips()
        tripsViewModel.tripsLiveData.observe(viewLifecycleOwner) {
            tripsViewModel.compareTripDatesWithToday()
            setupViewPager()
        }
    }

    private fun setupViewPager(){
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(ActiveTripsFragment.newInstance(Constants.ACTIVE_TRIPS), getString(R.string.active_trips))
        adapter.addFragment(ActiveTripsFragment.newInstance(Constants.ARCHIVE_TRIPS), getString(R.string.archive_trips))
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = adapter.getPageTitle(position)
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}