package kz.divtech.odyssey.rotation.ui.login.confirmdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentConfirmDataBinding

class ConfirmData : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentConfirmDataBinding.inflate(inflater)
        binding.confirmDataFragment = this
        return binding.root
    }
    fun openMainActivity(){
        val action = ConfirmDataDirections.actionConfirmDataToMainActivity()
        findNavController().navigate(action)
    }
}