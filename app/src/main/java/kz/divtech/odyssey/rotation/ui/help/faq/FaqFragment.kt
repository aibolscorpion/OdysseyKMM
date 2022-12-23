package kz.divtech.odyssey.rotation.ui.help.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kz.divtech.odyssey.rotation.databinding.FragmentFaqBinding

class FaqFragment : Fragment() {
    lateinit var binding : FragmentFaqBinding
    private val viewModel: FaqViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFaqBinding.inflate(inflater)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val faqAdapter = FaqAdapter()
        viewModel.getFaqList()

        binding.faqRecyclerView.adapter = faqAdapter
        viewModel.faqList.observe(viewLifecycleOwner) { faqList ->
            if(faqList != null && faqList.isNotEmpty()){
                faqAdapter.setList(faqList)
            }else{
                binding.pressServiceSearchView.visibility = View.GONE
                binding.noFAQ.root.visibility = View.VISIBLE
            }
        }
    }
}