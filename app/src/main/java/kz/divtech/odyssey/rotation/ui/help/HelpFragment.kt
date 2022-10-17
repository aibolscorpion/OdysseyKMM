package kz.divtech.odyssey.rotation.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val binding = FragmentHelpBinding.inflate(inflater)
        binding.helpFragment = this
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

    }

    fun openPressServiceFragment(){
        val action = HelpFragmentDirections.actionHelpFragmentToPressServiceFragment()
        findNavController().navigate(action)
    }
}