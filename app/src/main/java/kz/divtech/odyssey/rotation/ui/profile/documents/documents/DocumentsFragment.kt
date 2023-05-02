package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentDocumentsBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Document
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.ui.MainActivity

class DocumentsFragment : Fragment(), DocumentsAdapter.DocumentListener {
    private var currentEmployee: Employee? = null
    private val viewModel : DocumentsViewModel by viewModels{
        DocumentsViewModel.DocumentsViewModelFactory((activity as MainActivity).employeeRepository)
    }
    private var _binding: FragmentDocumentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDocumentsBinding.inflate(inflater)
        binding.documentsFragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DocumentsAdapter(this)
        binding.documentRV.adapter = adapter
        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            currentEmployee = employee
            binding.emptyDocuments.root.isVisible = employee.documents.isEmpty()
            adapter.setDocumentList(employee.documents)
        }
    }

    override fun onDocumentClicked(document: Document) {
        with(findNavController()){
            if(R.id.documentsFragment == currentDestination?.id){
                navigate(DocumentsFragmentDirections.actionDocumentsFragmentToDocumentDialog(
                    currentEmployee, document))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}