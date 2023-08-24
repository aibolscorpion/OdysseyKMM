package kz.divtech.odyssey.rotation.ui.profile.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.app.Constants.LNG_ENGLISH
import kz.divtech.odyssey.rotation.app.Constants.LNG_KAZAKH
import kz.divtech.odyssey.rotation.app.Constants.LNG_RUSSIAN
import kz.divtech.odyssey.rotation.databinding.FragmentChangeLanguageBinding
import kz.divtech.odyssey.rotation.utils.SharedPrefs.fetchAppLanguage
import kz.divtech.odyssey.rotation.utils.SharedPrefs.saveAppLanguage


class ChangeLanguageFragment: Fragment() {
    private var _dataBinding: FragmentChangeLanguageBinding? = null
    val dataBinding get() = _dataBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _dataBinding = FragmentChangeLanguageBinding.inflate(inflater, container, false)
        setCheckedRB()
        dataBinding.thisFragment = this
        return dataBinding.root
    }

    fun changeLanuage(){
        when(dataBinding.languagesRG.checkedRadioButtonId){
            dataBinding.kazakhRB.id -> requireContext().saveAppLanguage(LNG_KAZAKH)
            dataBinding.russianRB.id -> requireContext().saveAppLanguage(LNG_RUSSIAN)
            dataBinding.englishRB.id -> requireContext().saveAppLanguage(LNG_ENGLISH)
        }
        findNavController().popBackStack()
        activity?.recreate()
    }

    private fun setCheckedRB(){
        when(requireContext().fetchAppLanguage()){
            LNG_KAZAKH -> dataBinding.kazakhRB.isChecked = true
            LNG_RUSSIAN -> dataBinding.russianRB.isChecked = true
            LNG_ENGLISH -> dataBinding.englishRB.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
    }
}