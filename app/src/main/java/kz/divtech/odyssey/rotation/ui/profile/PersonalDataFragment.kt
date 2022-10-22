package kz.divtech.odyssey.rotation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentPersonalDataBinding

class PersonalDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPersonalDataBinding.inflate(inflater)
        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.maleRadioButton){
                changeRadioButtonBackground(binding.maleRadioButton, binding.femaleRadioButton)
            }else if(checkedId == R.id.femaleRadioButton){
                changeRadioButtonBackground(binding.femaleRadioButton, binding.maleRadioButton)
            }
        }

        ArrayAdapter.createFromResource(requireContext(), R.array.countries_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.countriesSpinner.adapter = adapter
        }

        activity?.findViewById<TextView>(R.id.toolbarTitleTV)?.setText(R.string.personal_data)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    private fun changeRadioButtonBackground(checkedRadioButton : RadioButton, uncheckedRadioButton: RadioButton){
        checkedRadioButton.background = AppCompatResources.getDrawable(requireContext(), R.drawable.bg_checked_radio_button)
        checkedRadioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        uncheckedRadioButton.background = AppCompatResources.getDrawable(requireContext(), R.drawable.bg_unchecked_radio_button)
        uncheckedRadioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.profile_menu_text))
    }

}