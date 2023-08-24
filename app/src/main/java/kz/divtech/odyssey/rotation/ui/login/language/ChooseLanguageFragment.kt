package kz.divtech.odyssey.rotation.ui.login.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants.LNG_ENGLISH
import kz.divtech.odyssey.rotation.app.Constants.LNG_KAZAKH
import kz.divtech.odyssey.rotation.app.Constants.LNG_RUSSIAN
import kz.divtech.odyssey.rotation.databinding.FragmentChooseLanguageBinding
import kz.divtech.odyssey.rotation.utils.SharedPrefs.saveAppLanguage
import kz.divtech.odyssey.rotation.utils.Utils.changeStatusBarColor
import kz.divtech.odyssey.rotation.utils.Utils.hideBottomNavigation
import kz.divtech.odyssey.rotation.utils.Utils.hideToolbar

class ChooseLanguageFragment: Fragment() {
    private var _binding: FragmentChooseLanguageBinding? = null
    val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChooseLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigation()
        hideToolbar()
        changeStatusBarColor(R.color.status_bar)
        binding.thisFragment = this
    }

    fun chooseLanguage(view: View){
        when(view.id){
            R.id.kazakhBtn -> requireContext().saveAppLanguage(LNG_KAZAKH)
            R.id.russianBtn -> requireContext().saveAppLanguage(LNG_RUSSIAN)
            R.id.englishBtn -> requireContext().saveAppLanguage(LNG_ENGLISH)
        }
        openPhoneNumberFragment()
        activity?.recreate()
    }

    private fun openPhoneNumberFragment(){
        findNavController().navigate(ChooseLanguageFragmentDirections.actionGlobalPhoneNumberFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}