package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentActiveTripsBinding
import kz.divtech.odyssey.rotation.domain.model.EmptyData
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.main.MainFragmentDirections

class ActiveTripsFragment : Fragment(), TripsAdapter.OnTripListener{

    val adapter = TripsAdapter(this)
    val viewModel: ActiveTripsViewModel by viewModels{
        ActiveTripsViewModel.TripsViewModelFactory((activity?.application as App).tripsRepository)
    }
    lateinit var binding: FragmentActiveTripsBinding

    companion object {
        fun newInstance(activeTrips: Boolean) =
            ActiveTripsFragment().apply{
                val bundle = Bundle()
                bundle.putBoolean(Constants.ACTIVE_TRIPS, activeTrips)
                arguments  = bundle
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActiveTripsBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.activeTripsRV.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activeTrips = arguments?.getBoolean(Constants.ACTIVE_TRIPS)
        viewModel.tripsLiveData.observe(viewLifecycleOwner){ data ->
            viewModel.divideIntoTwoParts(data)
        }

        if(activeTrips!!){
            observeLiveData(viewModel.activeTripsLiveData, EmptyData.ACTIVE_TRIPS)
        }else{
            observeLiveData(viewModel.archiveTripsLiveData, EmptyData.ARCHIVE_TRIPS)
        }
    }

    private fun observeLiveData(liveData: LiveData<List<Trip>>, emptyData: EmptyData){
        liveData.observe(viewLifecycleOwner) { list ->
            if(list.isNotEmpty()){
                binding.noTrips.root.visibility = View.GONE
            }else{
                binding.noTrips.root.visibility = View.VISIBLE
                binding.emptyData = emptyData
            }
            adapter.setTripList(list)
        }
    }

    override fun onTripClicked(trip: Trip) {
        if(trip.segments == null){
            findNavController().navigate(MainFragmentDirections.actionGlobalTicketsAreNotPurchasedDialog(trip))
        }else {
            findNavController().navigate(MainFragmentDirections.actionGlobalTripDetailDialog(trip))
        }
    }

    init {
        this.arguments = Bundle()
    }


}