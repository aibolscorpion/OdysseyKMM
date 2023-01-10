package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentActiveTripsBinding
import kz.divtech.odyssey.rotation.domain.model.main.EmptyData
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.main.MainFragmentDirections
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.adapters.TripsAdapter
import kotlin.collections.ArrayList

class ActiveTripsFragment : Fragment(), TripsAdapter.OnTripListener{
    val adapter = TripsAdapter(this)
    lateinit var binding: FragmentActiveTripsBinding
    companion object {
        fun newInstance(activeTrips: Boolean, tripList: ArrayList<Trip>): ActiveTripsFragment{
            val tripsFragment = ActiveTripsFragment()
            val bundle = Bundle()
            bundle.putBoolean(Constants.ACTIVE_TRIPS, activeTrips)
            bundle.putParcelableArrayList(Constants.TRIP_LIST, tripList)
            tripsFragment.arguments  = bundle
            return tripsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActiveTripsBinding.inflate(inflater)

        binding.activeTripsRV.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activeTrips = arguments?.getBoolean(Constants.ACTIVE_TRIPS)
        val list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(Constants.TRIP_LIST, Trip::class.java)
        } else {
            arguments?.getParcelableArrayList(Constants.TRIP_LIST)
        }

        if(list != null && list.isNotEmpty()){
            adapter.setTripList(list)
        }else{
            binding.noTrips.root.visibility = View.VISIBLE
            binding.emptyData =
                if(activeTrips == true) EmptyData.ACTIVE_TRIPS else EmptyData.ARCHIVE_TRIPS
        }
    }

    override fun onTripClicked(trip: Trip) {
        if(trip.segments == null){
            findNavController().navigate(MainFragmentDirections.actionGlobalTicketsAreNotPurchasedDialog(trip))
        }else {
            findNavController().navigate(MainFragmentDirections.actionGlobalTripDetailDialog(trip))
        }
    }


}