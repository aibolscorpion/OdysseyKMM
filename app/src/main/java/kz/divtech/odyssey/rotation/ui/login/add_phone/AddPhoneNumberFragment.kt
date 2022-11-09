package kz.divtech.odyssey.rotation.ui.login.add_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentAddPhoneBinding

class AddPhoneNumberFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = FragmentAddPhoneBinding.inflate(inflater)
        binding.addPhoneNumberFragment = this

        val args = AddPhoneNumberFragmentArgs.fromBundle(requireArguments())
        binding.employee = args.employee

        return binding.root
    }

    fun showPhoneNumberAddedDialog() =
        findNavController().navigate(AddPhoneNumberFragmentDirections.actionAddPhoneNumberToPhoneNumberAddedDialog())

    fun backToSearchByIINFragment() = findNavController().popBackStack()
}