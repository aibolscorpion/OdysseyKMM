package kz.divtech.odyssey.rotation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.databinding.FragmentDocumentBinding

class DocumentsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDocumentBinding.inflate(inflater)
        return binding.root
    }

}