package kz.divtech.odyssey.rotation.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val binding = FragmentHelpBinding.inflate(inflater)
        binding.helpFragment = this

        activity?.findViewById<TextView>(R.id.toolbarTitleTV)?.setText(R.string.help)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    fun writeSupport(){
        val action = HelpFragmentDirections.actionHelpFragmentToWriteSupportDialog()
        findNavController().navigate(action)
    }

    fun callSupport(){
        val action = HelpFragmentDirections.actionHelpFragmentToCallSupportDialog()
        findNavController().navigate(action)
    }

    fun openQAFragment(){
        val action = HelpFragmentDirections.actionHelpFragmentToQuestionsAnswersFragment()
        findNavController().navigate(action)
    }

    fun openPressServiceFragment(){
        val action = HelpFragmentDirections.actionHelpFragmentToPressServiceFragment()
        findNavController().navigate(action)
    }
}