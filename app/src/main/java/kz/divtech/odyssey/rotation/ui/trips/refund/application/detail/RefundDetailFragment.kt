package kz.divtech.odyssey.rotation.ui.trips.refund.application.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.databinding.FragmentRefundDetailBinding

class RefundDetailFragment : Fragment() {
    private var _binding: FragmentRefundDetailBinding? = null
    private val binding get() = _binding!!
    val args: RefundDetailFragmentArgs by navArgs()
    private val adapter by lazy { TicketAdapter(args.trip) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRefundDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refundAppItem = args.refundAppItem
        binding.ticketForRefundRV.adapter = adapter
        adapter.setTicketList(args.refundAppItem.segments)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}