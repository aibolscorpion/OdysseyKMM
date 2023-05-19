package kz.divtech.odyssey.rotation.ui.profile.personal_data.update_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentRefundSentBinding

class PhoneUpdatedFragment: Fragment(){
    private var _binding: FragmentRefundSentBinding? = null
    private val binding get() = _binding!!
    val args: PhoneUpdatedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRefundSentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToPrevPageBtn.apply {
            text = requireContext().getString(R.string.go_back)

            setOnClickListener {
                goToPersonalDataFragment()
            }
        }

        binding.apply {
            imageView.setImageResource(R.drawable.icon_phone_number_updated)
            titleTextView.text = getString(R.string.phone_number_successfully_updated)
            descriptionTextView.text = getString(
                R.string.use_new_phone_number_for_login, args.phoneNumber)
        }
    }

    private fun goToPersonalDataFragment() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}