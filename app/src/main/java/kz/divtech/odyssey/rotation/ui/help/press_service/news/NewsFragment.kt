package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentNewsBinding
import kz.divtech.odyssey.rotation.utils.RecyclerViewUtil.addItemDecorationWithoutLastDivider

class NewsFragment : Fragment(), NewsListener {
    val adapter: NewsPagingAdapter by lazy { NewsPagingAdapter(this) }

    val isRefreshing = ObservableBoolean()
    val viewModel: NewsViewModel by viewModels{
        NewsViewModel.ViewModelFactory((activity?.application as App).newsRepository)
    }
    lateinit var binding: FragmentNewsBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.thisFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNews()

//        viewModel.newsLiveData.observe(viewLifecycleOwner){ news ->
//            if(news.isNotEmpty()){
//                binding.noNews.root.visibility = View.GONE
//            }else{
//                binding.noNews.root.visibility = View.VISIBLE
//            }
//        }

        binding.newsSearchView.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    lifecycleScope.launch{
                        viewModel.searchNewsFromDB(newText).collectLatest { articleList ->
                            adapter.submitData(PagingData.from(articleList))
                        }
                    }
                }
                return true
            }

        })

    }

    private fun setNews(){
        binding.newsRecyclerView.adapter = adapter
        binding.newsRecyclerView.addItemDecorationWithoutLastDivider()

        lifecycleScope.launch{
            viewModel.getPagingNews().collectLatest {  pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    fun refreshNews(){
        isRefreshing.set(true)
        adapter.refresh()
        isRefreshing.set(false)
    }
    override fun onNewsClick(articleId: Int) {
        val action = NewsFragmentDirections.actionNewsFragmentToArticleDialog(articleId)
        findNavController().navigate(action)
    }

}