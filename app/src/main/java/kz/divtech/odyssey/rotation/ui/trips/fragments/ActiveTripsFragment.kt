package kz.divtech.odyssey.rotation.ui.trips.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kz.divtech.odyssey.rotation.databinding.FragmentActiveTripsBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.trips.adapters.TripsAdapter

class ActiveTripsFragment : Fragment(), TripsAdapter.OnTripListener{
    val viewModel by lazy { ViewModelProvider(this)[TripsViewModel::class.java]}
    val adapter = TripsAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentActiveTripsBinding.inflate(inflater)

        binding.activeTripsRV.adapter = adapter
        viewModel.tripsMutableLiveData.observe(viewLifecycleOwner) { trips ->
            adapter.setTripList(trips.data?.data!!)
        }

        viewModel.visibility.observe(viewLifecycleOwner){ visibility ->
            binding.visibility = visibility
        }

        viewModel.getTrips()


        return binding.root
    }

    override fun onTripClicked(trip: Trip) {
//        val action = if(trip.isTicketPurchased){
//            TripsFragmentDirections.actionTripsFragmentToTripDetailDialog(trip)
//        }
//        else {
//            TripsFragmentDirections.actionTripsFragmentToTicketsAreNotPurchasedDialog(trip)
//        }
//        findNavController().navigate(action)
    }

}