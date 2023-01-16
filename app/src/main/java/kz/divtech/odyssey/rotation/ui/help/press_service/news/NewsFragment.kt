package kz.divtech.odyssey.rotation.ui.help.press_service.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentNewsBinding
import kz.divtech.odyssey.rotation.utils.Utils.addItemDecorationWithoutLastDivider

class NewsFragment : Fragment(), NewsListener {
    val viewModel: NewsViewModel by viewModels{
        NewsViewModel.ViewModelFactory((activity?.application as App).newsRepository)
    }
    lateinit var binding: FragmentNewsBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewsBinding.inflate(inflater)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsAdapter(this)
        binding.newsRecyclerView.adapter = adapter
        binding.newsRecyclerView.addItemDecorationWithoutLastDivider()

        viewModel.newsLiveData.observe(viewLifecycleOwner){ news ->
            if(news.isNotEmpty()){
                adapter.setNews(news)
                binding.noNews.root.visibility = View.GONE
            }else{
                viewModel.getNewsFromServer()
                binding.noNews.root.visibility = View.VISIBLE
            }
        }

        binding.newsSearchView.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    viewModel.searchNewsFromDB(newText).observe(viewLifecycleOwner) { articleList ->
                        adapter.setNews(articleList)
                    }
                }
                return true
            }

        })

    }

    override fun onNewsClick(articleId: Int) {
        val action = NewsFragmentDirections.actionNewsFragmentToArticleDialog(articleId)
        findNavController().navigate(action)
    }

}