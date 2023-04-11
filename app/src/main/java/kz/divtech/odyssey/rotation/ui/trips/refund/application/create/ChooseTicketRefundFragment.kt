package kz.divtech.odyssey.rotation.ui.trips.refund.application.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentChooseTicketRefundBinding

class ChooseTicketRefundFragment : Fragment(), TicketAdapter.OnItemCheckListener {
    private var _binding: FragmentChooseTicketRefundBinding? = null
    private val binding get() = _binding!!
    private val args: ChooseTicketRefundFragmentArgs by navArgs()
    private val segmentIdList = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentChooseTicketRefundBinding.inflate(inflater)
        binding.thisDialog = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TicketAdapter(this)
        binding.ticketsRV.adapter = adapter
        adapter.setTicketList(args.issuedTickets.toList())
    }

    fun openReasonForRefundFragment(){
        if(segmentIdList.isNotEmpty()){
            findNavController().navigate(ChooseTicketRefundFragmentDirections.
            actionChooseTicketRefundFragmentToRefundReasonFragment(
                args.issuedTickets[0].application_id, segmentIdList.toIntArray()))
        }else{
            Toast.makeText(requireContext(), R.string.choose_ticket_for_refund, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemCheck(segmentId: Int) {
        segmentIdList.add(segmentId)
    }

    override fun onItemUncheck(segmentId: Int) {
        segmentIdList.remove(segmentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}