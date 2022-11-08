package kz.divtech.odyssey.rotation.ui.profile.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentDocumentBinding

class DocumentsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentDocumentBinding.inflate(inflater)
        binding.documentsFragment = this

        return binding.root
    }

    fun showWorkPermissionDialog() =
        findNavController().navigate(DocumentsFragmentDirections.actionDocumentsFragmentToWorkPermissionDialog())

    fun showIdDialog() =
        findNavController().navigate(DocumentsFragmentDirections.actionDocumentsFragmentToIdDialog())

    fun showPassportDialog() =
        findNavController().navigate(DocumentsFragmentDirections.actionDocumentsFragmentToPassportDialog())

    fun showRecidencyPermitDialog() =
        findNavController().navigate(DocumentsFragmentDirections.actionDocumentsFragmentToRecidencyPermitDialog())

}