package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentNewsBinding
import kz.divtech.odyssey.rotation.domain.model.EmptyData
import kz.divtech.odyssey.rotation.ui.help.press_service.news.paging.NewsListener
import kz.divtech.odyssey.rotation.ui.help.press_service.news.paging.NewsPagingAdapter
import kz.divtech.odyssey.rotation.ui.profile.notification.paging.LoaderAdapter
import kz.divtech.odyssey.rotation.common.utils.RecyclerViewUtil.addItemDecorationWithoutLastDivider
import java.net.UnknownHostException

@AndroidEntryPoint
class NewsFragment : Fragment(), NewsListener, LoaderAdapter.RetryCallback {
    val adapter: NewsPagingAdapter by lazy { NewsPagingAdapter(this) }
    val isRefreshing = ObservableBoolean()

    val viewModel: NewsViewModel by viewModels()

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private var loadStateJob : Job? = null


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.thisFragment = this
        binding.emptyData = EmptyData(ContextCompat.getDrawable(requireContext(), R.drawable.icon_news)!!,
            requireContext().getString(R.string.empty_news_title),
            requireContext().getString(R.string.empty_news_content))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNews()
        setSearch()
        loadState()
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.press_service))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "NewsFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    private fun setNews(){
        binding.newsRecyclerView.adapter = adapter.withLoadStateFooter(LoaderAdapter(this))
        binding.newsRecyclerView.addItemDecorationWithoutLastDivider()

        lifecycleScope.launch{
            viewModel.getPagingNews().collectLatest {  pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun setSearch(){
        binding.newsSearchView.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    lifecycleScope.launch{
                        val articleList = viewModel.searchNewsFromDB(newText)
                        adapter.submitData(PagingData.from(articleList))
                    }
                }
                return true
            }

        })
    }

    private fun loadState(){
        loadStateJob = lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest{ loadState ->

                val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                binding.emptyNews.root.isVisible = isListEmpty

                binding.newsRecyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading

                binding.newsPBar.isVisible = loadState.mediator?.refresh is LoadState.Loading

                binding.newsRetryBtn.isVisible = loadState.mediator?.refresh is LoadState.Error
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

    fun refreshNews(){
        isRefreshing.set(true)
        adapter.refresh()
        isRefreshing.set(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        loadStateJob?.cancel()
        _binding = null
    }

    override fun onNewsClick(articleId: Int) {
        with(findNavController()){
            if(R.id.newsFragment == currentDestination?.id){
                navigate(NewsFragmentDirections.actionNewsFragmentToArticleDialog(articleId))
            }
        }
    }

    override fun onRetryClicked() {
        adapter.retry()
    }

}