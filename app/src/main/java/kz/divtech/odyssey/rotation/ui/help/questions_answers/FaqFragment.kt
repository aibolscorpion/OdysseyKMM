package kz.divtech.odyssey.rotation.ui.help.questions_answers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kz.divtech.odyssey.rotation.databinding.FragmentFaqBinding
import kz.divtech.odyssey.rotation.viewmodels.help.FaqViewModel

class FaqFragment : Fragment() {
    lateinit var binding : FragmentFaqBinding
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFaqBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val faqAdapter = FaqAdapter()
        val viewModel = ViewModelProvider(this)[FaqViewModel::class.java]
        viewModel.getFaqList()

        binding.faqRecyclerView.adapter = faqAdapter
        viewModel.faqList.observe(viewLifecycleOwner) {
            faqAdapter.setList(it)
        }

    }

}