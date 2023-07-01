package kz.divtech.odyssey.rotation.ui.profile.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.databinding.FragmentNotificationBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.LoaderAdapter
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.NotificationListener
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.NotificationPagingAdapter
import java.net.UnknownHostException

class NotificationFragment : Fragment(), NotificationListener, LoaderAdapter.RetryCallback {
    val isRefreshing = ObservableBoolean()
    val viewModel: NotificationViewModel by viewModels {
        NotificationViewModel.NotificationViewModelFactory(
            (activity as MainActivity).notificationRepository)
    }
    val adapter: NotificationPagingAdapter by lazy { NotificationPagingAdapter(this) }
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private var loadStateJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentNotificationBinding.inflate(inflater)

        binding.thisFragment = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTripsPagingAdapter()
        loadState()
    }


    private fun setupTripsPagingAdapter() {
        binding.notificationRecyclerView.adapter = adapter.withLoadStateFooter(LoaderAdapter(this))
        lifecycleScope.launch {
            viewModel.getPagingNotifications().collectLatest { notifications ->
                adapter.submitData(notifications)
            }
        }

    }

    private fun loadState(){
        loadStateJob = lifecycleScope.launch {
            adapter.loadStateFlow.collect{ loadState ->

                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.emptyNotifications.root.isVisible = isListEmpty

                binding.notificationRecyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading

                binding.notificationsPBar.isVisible = loadState.mediator?.refresh is LoadState.Loading

                binding.notificationsRetryBtn.isVisible = loadState.mediator?.refresh is LoadState.Error
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

    fun refreshNotifications(){
        isRefreshing.set(true)
        adapter.refresh()
        isRefreshing.set(false)
    }

    override fun onNotificationClicked(notification: Notification){
        findNavController().navigate(
            NotificationFragmentDirections.actionGlobalNotificationDialog(
                notification.convertToPushNotification()))
    }


    override fun onRetryClicked() {
        adapter.retry()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        loadStateJob?.cancel()
        _binding = null
    }
}