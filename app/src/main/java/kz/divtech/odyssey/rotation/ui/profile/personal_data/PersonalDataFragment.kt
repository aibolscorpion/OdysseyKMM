package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kz.divtech.odyssey.rotation.databinding.FragmentPersonalDataBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee

class PersonalDataFragment : Fragment() {
    val viewModel : PersonalDataViewModel by viewModels()
    lateinit var binding: FragmentPersonalDataBinding
    lateinit var employee: Employee


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPersonalDataBinding.inflate(inflater)
        employee = PersonalDataFragmentArgs.fromBundle(requireArguments()).employee

        binding.employee = employee
        binding.personalDataFragment = this
        binding.viewModel = viewModel

        return binding.root
    }

}