package kz.divtech.odyssey.rotation.ui.trips.refund.application_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.databinding.FragmentRefundDetailBinding
import kz.divtech.odyssey.rotation.ui.trips.refund.create_application.TicketAdapter

class RefundDetailFragment : Fragment() {
    private val adapter by lazy { TicketAdapter() }
    private var _binding: FragmentRefundDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRefundDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ticketForRefundRV.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}