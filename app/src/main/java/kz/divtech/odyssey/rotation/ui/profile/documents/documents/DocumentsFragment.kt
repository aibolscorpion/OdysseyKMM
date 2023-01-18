package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.databinding.FragmentDocumentsBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Document

class DocumentsFragment : Fragment(), DocumentsAdapter.DocumentListener {
    private var currentEmployee: Employee? = null
    private val viewModel : DocumentsViewModel by viewModels{
        DocumentsViewModel.DocumentsViewModelFactory((activity?.application as App).employeeRepository,
            (activity?.application as App).documentRepository)
    }
    private lateinit var binding: FragmentDocumentsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDocumentsBinding.inflate(inflater)
        binding.documentsFragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            currentEmployee = employee
        }

        val adapter = DocumentsAdapter(this)
        binding.documentRV.adapter = adapter

        viewModel.documentsLiveData.observe(viewLifecycleOwner){ documents ->
            if(documents.isNotEmpty()){
                binding.noDocuments.root.visibility = View.GONE
                adapter.setDocumentList(documents)
            }else{
                viewModel.getDocumentsFromServer()
                binding.noDocuments.root.visibility = View.VISIBLE
            }
        }
    }

    override fun onDocumentClicked(document: Document) {
        val action = DocumentsFragmentDirections.actionDocumentsFragmentToDocumentDialog(
            document, currentEmployee)
        findNavController().navigate(action)
    }

}