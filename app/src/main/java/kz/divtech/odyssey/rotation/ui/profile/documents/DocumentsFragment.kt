package kz.divtech.odyssey.rotation.ui.profile.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentDocumentBinding

class DocumentsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDocumentBinding.inflate(inflater)
        binding.documentsFragment = this

        activity?.findViewById<TextView>(R.id.toolbarTitleTV)?.setText(R.string.documents)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    fun showWorkPermissionDialog(){
        val action = DocumentsFragmentDirections.actionDocumentsFragmentToWorkPermissionDialog()
        findNavController().navigate(action)
    }

    fun showIdDialog(){
        val action = DocumentsFragmentDirections.actionDocumentsFragmentToIdDialog()
        findNavController().navigate(action)

    }

    fun showPassportDialog(){
        val action = DocumentsFragmentDirections.actionDocumentsFragmentToPassportDialog()
        findNavController().navigate(action)

    }

    fun showRecidencyPermitDialog(){
        val action = DocumentsFragmentDirections.actionDocumentsFragmentToRecidencyPermitDialog()
        findNavController().navigate(action)
    }

}