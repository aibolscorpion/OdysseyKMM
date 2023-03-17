package kz.divtech.odyssey.rotation.ui.trips.refund.create_application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentChooseTicketRefundBinding

class ChooseTicketRefundFragment : Fragment() {
    private val adapter: TicketAdapter by lazy { TicketAdapter() }
    private var _binding: FragmentChooseTicketRefundBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentChooseTicketRefundBinding.inflate(inflater)
        binding.thisDialog = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ticketsRV.adapter = adapter
    }

    fun openReasonForRefundFragment(){
        findNavController().navigate(ChooseTicketRefundFragmentDirections.
            actionChooseTicketRefundFragmentToRefundReasonFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}