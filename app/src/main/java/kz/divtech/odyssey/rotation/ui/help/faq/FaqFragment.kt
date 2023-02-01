package kz.divtech.odyssey.rotation.ui.help.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentFaqBinding

class FaqFragment : Fragment() {
    lateinit var binding : FragmentFaqBinding
    internal val viewModel: FaqViewModel by viewModels {
            FaqViewModel.FaqViewModelFactory((activity?.application as App).faqRepository)
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFaqBinding.inflate(inflater)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val faqAdapter = FaqAdapter()

        binding.faqRecyclerView.adapter = faqAdapter
        viewModel.getFaqListFromServer()

        viewModel.faqLiveData.observe(viewLifecycleOwner) { faqList ->
            binding.faqSearchView.isVisible = faqList.isNotEmpty()
            binding.emptyFaq.root.isVisible = faqList.isEmpty()
            faqAdapter.setList(faqList)
        }

        binding.faqSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchFaqFromDB(it).observe(viewLifecycleOwner) { faqList ->
                        faqAdapter.setList(faqList)
                    }
                }
                return true
            }

        })
    }
}