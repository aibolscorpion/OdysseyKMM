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
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants.ID_CARD
import kz.divtech.odyssey.rotation.common.Constants.KAZAKHSTAN_CODE
import kz.divtech.odyssey.rotation.common.Constants.PASSPORT
import kz.divtech.odyssey.rotation.common.Constants.RESIDENCE
import kz.divtech.odyssey.rotation.common.Constants.FOREIGN
import kz.divtech.odyssey.rotation.databinding.FragmentDocumentsBinding
import kz.divtech.odyssey.shared.domain.model.profile.Document
import kz.divtech.odyssey.shared.domain.model.profile.Profile

@AndroidEntryPoint
class DocumentsFragment : Fragment(), DocumentsAdapter.DocumentListener {
    private val citizenDocumentList = listOf(ID_CARD, PASSPORT)
    private val foreignerDocumentList = listOf(PASSPORT, RESIDENCE, FOREIGN)

    private var currentProfile: Profile? = null
    private val viewModel : DocumentsViewModel by viewModels()
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
        viewModel.employeeLiveData.observe(viewLifecycleOwner){ profile ->
            profile?.let {
                currentProfile = profile
                if(profile.countryCode == KAZAKHSTAN_CODE){
                    adapter.setUserDocumentList(profile.documents, citizenDocumentList)
                }else{
                    adapter.setUserDocumentList(profile.documents, foreignerDocumentList)
                }
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
                    currentProfile, document))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}