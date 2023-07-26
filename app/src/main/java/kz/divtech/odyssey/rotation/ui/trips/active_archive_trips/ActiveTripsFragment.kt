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
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentActiveTripsBinding
import kz.divtech.odyssey.rotation.domain.model.EmptyData
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.LoaderAdapter
import kz.divtech.odyssey.rotation.ui.trips.TripsFragmentDirections
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.FilterTripDialog
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.OnFilterClicked
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripDialog
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripType.BY_STATUS
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripType.BY_DEPARTURE_DATE
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.paging.TripsPagingAdapter
import java.net.UnknownHostException

class ActiveTripsFragment : Fragment(), TripsPagingAdapter.OnTripListener, LoaderAdapter.RetryCallback,
    SortTripDialog.OnTripSortClicked, OnFilterClicked {
    val refreshing = ObservableBoolean()
    val adapter: TripsPagingAdapter by lazy { TripsPagingAdapter(this) }
    val viewModel: ActiveTripsViewModel by viewModels{
        ActiveTripsViewModel.TripsViewModelFactory((activity as MainActivity).tripsRepository)
    }
    private var _binding: FragmentActiveTripsBinding? = null
    private val binding get() = _binding!!
    private var isActiveTrips: Boolean? = null

    companion object {
        fun newInstance(activeTrips: Boolean) =
            ActiveTripsFragment().apply{
                arguments  = bundleOf(Constants.ACTIVE_TRIPS to activeTrips)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentActiveTripsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.thisFragment = this
        setupTripsPagingAdapter()
        loadStates()
    }

    private fun setupTripsPagingAdapter(){
        binding.tripsRV.adapter = adapter.withLoadStateFooter(LoaderAdapter(this))
        isActiveTrips = arguments?.getBoolean(Constants.ACTIVE_TRIPS)
        refreshTrips()
    }

    private fun loadStates(){
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
                    if(errorState.error !is UnknownHostException){
                        Toast.makeText(requireContext(), errorState.error.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    fun refreshTrips(){
        refreshing.set(true)
        lifecycleScope.launch{
            isActiveTrips?.let { viewModel.refreshTrips(it).collectLatest { pagingData ->
                adapter.submitData(pagingData)
                binding.tripsRV.scrollToPosition(0)
                refreshing.set(false)
            }}
        }

    }

    fun openSortTripDialog(){
        val sortTripDialog = viewModel.sortType.value?.let { SortTripDialog(this, it) }
        sortTripDialog?.show(activity?.supportFragmentManager!!, "SortTripDialog")

        viewModel.sortType.observe(viewLifecycleOwner){ type ->
            when(type){
                BY_STATUS -> binding.sortTripTV.text = getString(R.string.sort_by_status)
                else -> binding.sortTripTV.text = getString(R.string.sort_by_departure_date)
            }
        }
    }

    fun openFilterTripDialog(){
        val filterTripDialog = FilterTripDialog(this)
        filterTripDialog.show(childFragmentManager, "FilterTripDialog")

        viewModel.appliedFilterCount.observe(viewLifecycleOwner) { appliedFilterCount ->
            binding.appliedFilterCountTV.isVisible = appliedFilterCount != 0
            binding.appliedFilterCountTV.text = appliedFilterCount.toString()
        }
    }

    override fun onTripClicked(trip: Trip?) {
        trip?.let {
            with(findNavController()){
                if(R.id.tripsFragment == currentDestination?.id){
                    if(trip.segments.isEmpty()){
                        navigate(TripsFragmentDirections.actionGlobalTicketsAreNotPurchasedDialog(trip))
                    }else {
                        navigate(TripsFragmentDirections.actionGlobalTripDetailDialog(trip))
                    }
                }
            }
        }
    }

    init {
        this.arguments = Bundle()
    }

    override fun onRetryClicked() {
        adapter.retry()
    }

    override fun onDateClicked() {
        binding.sortTripTV.text = getString(R.string.sort_by_departure_date)
        viewModel.setSortType(BY_DEPARTURE_DATE)
        viewModel.resetFilter()
        refreshTrips()
    }

    override fun onStatusClicked() {
        viewModel.setSortType(BY_STATUS)
        viewModel.resetFilter()
        refreshTrips()
    }

    override fun applyFilterClicked() {
        viewModel.setSortType(BY_DEPARTURE_DATE)
        refreshTrips()
    }

}