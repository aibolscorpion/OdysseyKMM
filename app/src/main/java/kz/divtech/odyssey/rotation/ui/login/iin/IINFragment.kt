package kz.divtech.odyssey.rotation.ui.login.iin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentIinBinding

class IINFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentIinBinding.inflate(inflater)
        showEmployeeNotFoundDialog()
        binding.iinFragment = this
        return binding.root
    }

    fun showEmployeeNotFoundDialog(){
        val action = IINFragmentDirections.actionIINFragmentToEmployeeNotFoundDialog()
        findNavController().navigate(action)
    }

    fun backToCodeFragment(){
        findNavController().popBackStack()
    }

    fun openChangeNumberFragment(){
        val action = IINFragmentDirections.actionIINFragmentToChangeNumberFragment()
        findNavController().navigate(action)
    }
}