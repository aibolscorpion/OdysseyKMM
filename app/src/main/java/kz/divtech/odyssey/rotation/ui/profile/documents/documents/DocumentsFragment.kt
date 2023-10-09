package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants.ID_CARD
import kz.divtech.odyssey.rotation.common.Constants.KAZAKHSTAN_CODE
import kz.divtech.odyssey.rotation.common.Constants.PASSPORT
import kz.divtech.odyssey.rotation.common.Constants.RESIDENCE
import kz.divtech.odyssey.rotation.common.Constants.FOREIGN
import kz.divtech.odyssey.rotation.databinding.FragmentDocumentsBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Document
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.ui.MainActivity

class DocumentsFragment : Fragment(), DocumentsAdapter.DocumentListener {
    private val citizenDocumentList = listOf(ID_CARD, PASSPORT)
    private val foreignerDocumentList = listOf(PASSPORT, RESIDENCE, FOREIGN)

    private var currentEmployee: Employee? = null
    private val viewModel : DocumentsViewModel by viewModels{
        DocumentsViewModel.DocumentsViewModelFactory((activity as MainActivity).employeeRepository)
    }
    private var _binding: FragmentDocumentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDocumentsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DocumentsAdapter(this)
        binding.documentsRV.adapter = adapter
        viewModel.employeeLiveData.observe(viewLifecycleOwner){ employee ->
            currentEmployee = employee
            if(employee.country_code == KAZAKHSTAN_CODE){
                adapter.setUserDocumentList(employee.documents, citizenDocumentList)
            }else{
                adapter.setUserDocumentList(employee.documents, foreignerDocumentList)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.documents))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "DocumentsFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
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