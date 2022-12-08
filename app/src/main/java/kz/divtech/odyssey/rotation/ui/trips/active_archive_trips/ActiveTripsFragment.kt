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
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.trips.TripsFragmentDirections
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.adapters.TripsAdapter

class ActiveTripsFragment : Fragment(), TripsAdapter.OnTripListener{
    val adapter = TripsAdapter(this)
    companion object {
        fun newInstance(tripList: ArrayList<Trip>): ActiveTripsFragment{
            val tripsFragment = ActiveTripsFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(Constants.TRIP_LIST, tripList)
            tripsFragment.arguments  = bundle
            return tripsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentActiveTripsBinding.inflate(inflater)

        binding.activeTripsRV.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(Constants.TRIP_LIST, Trip::class.java)
        } else {
            arguments?.getParcelableArrayList(Constants.TRIP_LIST)
        }
        adapter.setTripList(list!!)
    }

    override fun onTripClicked(trip: Trip) {
        val action = if(trip.segments == null){
            TripsFragmentDirections.actionTripsFragmentToTicketsAreNotPurchasedDialog(trip)
        }else {
            TripsFragmentDirections.actionTripsFragmentToTripDetailDialog(trip)
        }
        findNavController().navigate(action)
    }


}