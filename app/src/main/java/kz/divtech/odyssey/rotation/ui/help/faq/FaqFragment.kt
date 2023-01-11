package kz.divtech.odyssey.rotation.ui.help.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentFaqBinding

class FaqFragment : Fragment() {
    lateinit var binding : FragmentFaqBinding
    private val viewModel: FaqViewModel by viewModels {
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
        viewModel.faqLiveData.observe(viewLifecycleOwner) { faqList ->
            if(faqList.isNotEmpty()){
                binding.pressServiceSearchView.visibility = View.VISIBLE
                binding.noFAQ.root.visibility = View.GONE
                faqAdapter.setList(faqList)
            }else{
                viewModel.getFaqListFromServer()
                binding.pressServiceSearchView.visibility = View.GONE
                binding.noFAQ.root.visibility = View.VISIBLE
            }
        }
    }
}