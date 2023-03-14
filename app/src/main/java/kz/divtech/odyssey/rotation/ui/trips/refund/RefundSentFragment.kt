package kz.divtech.odyssey.rotation.ui.trips.refund

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentRefundSentBinding

class RefundSentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentRefundSentBinding.inflate(inflater)
        binding.thisDialog = this
        return binding.root
    }

    fun openTripsFragment() = findNavController().navigate(
        RefundSentFragmentDirections.actionRefundSentFragmentToTripsFragment())

}