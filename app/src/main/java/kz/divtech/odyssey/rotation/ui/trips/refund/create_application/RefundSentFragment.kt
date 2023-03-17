package kz.divtech.odyssey.rotation.ui.trips.refund.create_application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentRefundSentBinding

class RefundSentFragment : Fragment() {
    private var _binding: FragmentRefundSentBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RefundSentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRefundSentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.thisDialog = this
        when(args.refundSent){
            true -> {
                binding.refundApplicationIV.setImageResource(R.drawable.icon_refund_sent)
                binding.refundSentTitleTV.text = getString(R.string.application_to_refund_sent_title)
                binding.refundSentDescriptionTV.text = getString(
                    R.string.application_to_refund_sent_description, args.refundNumber)
            }
            false -> {
                binding.refundApplicationIV.setImageResource(R.drawable.icon_refund_cancelled_2)
                binding.refundSentTitleTV.text = getString(R.string.application_to_refund_cancelled_title)
                binding.refundSentDescriptionTV.text = getString(
                    R.string.application_to_refund_cancelled_description, args.refundNumber)
            }
        }

    }

    fun openTripsFragment() = findNavController().navigate(
        RefundSentFragmentDirections.actionRefundSentFragmentToTripsFragment())

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}