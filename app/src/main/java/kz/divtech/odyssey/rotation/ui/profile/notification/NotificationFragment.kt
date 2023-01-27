package kz.divtech.odyssey.rotation.ui.profile.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentNotificationBinding
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification

class NotificationFragment : Fragment(), NotificationListener{
    val isRefreshing = ObservableBoolean()
    val viewModel: NotificationViewModel by viewModels {
        NotificationViewModel.NotificationViewModelFactory(
            (activity?.application as App).notificationRepository)
    }

    private var adapter: NotificationPagingAdapter? = null
    lateinit var binding: FragmentNotificationBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding = FragmentNotificationBinding.inflate(inflater)

        binding.thisFragment = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()

    }

    private fun initView() {
        adapter = NotificationPagingAdapter(this)
        binding.notificationRecyclerView.adapter = adapter
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            viewModel.getNotificationsFromServer().collectLatest { notifications ->
                adapter?.submitData(notifications)
            }
        }
    }

    fun refreshNotifications(){
        isRefreshing.set(true)
        adapter?.refresh()
        isRefreshing.set(false)
    }

    override fun onNotificationClicked(notification: Notification) =
        findNavController().navigate(
            NotificationFragmentDirections.actionGlobalNotificationDialog(notification))
}