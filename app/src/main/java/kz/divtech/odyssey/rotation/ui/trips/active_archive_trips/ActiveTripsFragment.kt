package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentActiveTripsBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.trips.TripsFragmentDirections
import kz.divtech.odyssey.rotation.ui.trips.TripsViewModel
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.adapters.TripsAdapter

class ActiveTripsFragment : Fragment(), TripsAdapter.OnTripListener{
    val viewModel by lazy { ViewModelProvider(requireActivity())[TripsViewModel::class.java]}
    val adapter = TripsAdapter(this)

    companion object {
        fun newInstance(tripType: Int): ActiveTripsFragment{
            val tripsFragment = ActiveTripsFragment()
            tripsFragment.arguments = bundleOf(Constants.TRIP_TYPE to tripType)
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

        when(arguments?.getInt(Constants.TRIP_TYPE)){
            Constants.ACTIVE_TRIPS -> adapter.setTripList(viewModel.activeTrips)
            Constants.ARCHIVE_TRIPS -> adapter.setTripList(viewModel.archiveTrips)
        }
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