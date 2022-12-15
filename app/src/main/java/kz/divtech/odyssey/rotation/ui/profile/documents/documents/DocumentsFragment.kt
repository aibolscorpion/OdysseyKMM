package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kz.divtech.odyssey.rotation.databinding.FragmentDocumentBinding
import kz.divtech.odyssey.rotation.domain.model.profile.Document

class DocumentsFragment : Fragment(), DocumentsAdapter.DocumentClickListener {
    private val viewModel : DocumentsViewModel by viewModels()
    private lateinit var binding: FragmentDocumentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDocumentBinding.inflate(inflater)
        binding.documentsFragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DocumentsAdapter(this)
        binding.documentRV.adapter = adapter

        viewModel.getAllDocuments()
        viewModel.documents.observe(viewLifecycleOwner){ documents ->
            if(documents.documents != null){
                adapter.setDocumentList(documents.documents)
            }
        }

    }

    override fun onDocumentClicked(document: Document) {

    }

}