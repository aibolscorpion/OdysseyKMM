package kz.divtech.odyssey.rotation.ui.profile.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentNotificationBinding
import kz.divtech.odyssey.shared.domain.model.EmptyData
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.LoaderAdapter
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.NotificationListener
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.NotificationPagingAdapter
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.profile.notifications.convertToPushNotification
import java.net.UnknownHostException

@AndroidEntryPoint
class NotificationFragment : Fragment(), NotificationListener, LoaderAdapter.RetryCallback {
    val isRefreshing = ObservableBoolean()
    val viewModel: NotificationViewModel by viewModels()
    val adapter: NotificationPagingAdapter by lazy { NotificationPagingAdapter(this) }
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private var loadStateJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentNotificationBinding.inflate(inflater)

        binding.thisFragment = this
        binding.viewModel = viewModel
        binding.emptyData = EmptyData(ContextCompat.getDrawable(requireContext(), R.drawable.icon_notifications)!!,
            requireContext().getString(R.string.empty_notifications_title),
            requireContext().getString(R.string.empty_notifications_content))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTripsPagingAdapter()
        loadState()
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.notification))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "NotificationFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
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