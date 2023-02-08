package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentActiveTripsBinding
import kz.divtech.odyssey.rotation.domain.model.EmptyData
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.main.MainFragmentDirections
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.LoaderAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.paging.TripsPagingAdapter

class ActiveTripsFragment : Fragment(), TripsPagingAdapter.OnTripListener, LoaderAdapter.RetryCallback{
    val refreshing = ObservableBoolean()
    val adapter: TripsPagingAdapter by lazy { TripsPagingAdapter(this) }
    val viewModel: ActiveTripsViewModel by viewModels{
        ActiveTripsViewModel.TripsViewModelFactory((activity as MainActivity).tripsRepository)
    }
    lateinit var binding: FragmentActiveTripsBinding

    companion object {
        fun newInstance(activeTrips: Boolean) =
            ActiveTripsFragment().apply{
                arguments  = bundleOf(Constants.ACTIVE_TRIPS to activeTrips)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActiveTripsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.thisFragment = this

        setupTripsPagingAdapter()
        loadStates()

    }

    private fun setupTripsPagingAdapter(){
        val isActiveTrips = arguments?.getBoolean(Constants.ACTIVE_TRIPS)

        binding.tripsRV.adapter = adapter.withLoadStateFooter(LoaderAdapter(this))
        lifecycleScope.launch{
            if(isActiveTrips!!){
                viewModel.getActivePagingTrips().collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }else{
                viewModel.getArchivePagingTrips().collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    private fun loadStates(){
        val isActiveTrips = arguments?.getBoolean(Constants.ACTIVE_TRIPS)
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest{ loadState ->

                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.emptyData = if(isActiveTrips!!) EmptyData.ACTIVE_TRIPS else EmptyData.ARCHIVE_TRIPS
                binding.emptyTrips.root.isVisible = isListEmpty

                binding.tripsRV.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading

                binding.tripsPBar.isVisible = loadState.mediator?.refresh is LoadState.Loading

                binding.tripsRetryBtn.isVisible = loadState.mediator?.refresh is LoadState.Error
                        && adapter.itemCount == 0

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

                errorState?.let {
                    Toast.makeText(requireContext(), errorState.error.toString(),
                        Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun refreshTrips(){
        refreshing.set(true)
        adapter.refresh()
        refreshing.set(false)
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

    override fun onRetryClicked() {
        adapter.retry()
    }


}