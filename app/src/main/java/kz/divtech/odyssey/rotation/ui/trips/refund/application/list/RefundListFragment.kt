package kz.divtech.odyssey.rotation.ui.trips.refund.application.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.databinding.FragmentRefundListBinding
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.trips.refund.application.RefundViewModel

class RefundListFragment : Fragment(), RefundListAdapter.RefundBtnClick {
    private var _binding: FragmentRefundListBinding? = null
    private val binding get() = _binding!!
    private val args: RefundListFragmentArgs by navArgs()
    val adapter : RefundListAdapter by lazy { RefundListAdapter(this) }
    val viewModel: RefundViewModel by viewModels{
        RefundViewModel.RefundViewModelFactory((activity as MainActivity).refundRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRefundListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.thisDialog = this

        viewModel.getRefundListByTripId(args.trip.id)
        viewModel.refundListLiveData.observe(viewLifecycleOwner) {
            if(it.isSuccess()){
                assignPendingStatusToIssuedSegments(it.asSuccess().value)
                binding.createRefundAppTV.isVisible = isShownCreateRefundBtn()
                adapter.setRefundList(getRefundListWithRealSegment(it.asSuccess().value))
            }else{
                Toast.makeText(requireContext(), R.string.error_happened, Toast.LENGTH_SHORT).show()
            }
        }
        binding.refundListRV.adapter = adapter
    }

    private fun getRefundListWithRealSegment(refundAppList: ArrayList<RefundAppItem>) : ArrayList<RefundAppItem> {
        refundAppList.forEach { refundAppItem ->
            val realSegment = mutableListOf<Segment>()
            refundAppItem.segments.forEach { refundSegment ->
                args.trip.segments.forEach { segment ->
                    if(segment.id == refundSegment.segment_id){
                        realSegment.add(segment)
                    }
                }
            }
            refundAppItem.realSegment = realSegment
        }
        return refundAppList
    }

    private fun assignPendingStatusToIssuedSegments(refundList: List<RefundAppItem>){
        args.issuedSegmentList?.forEach { segment ->
            refundList.forEach { refundAppItem ->
                if (refundAppItem.status == Constants.REFUND_STATUS_PENDING) {
                    refundAppItem.segments.forEach { refundSegment ->
                        if (segment.id == refundSegment.segment_id) {
                            segment.status = Constants.REFUND_STATUS_PENDING
                        }
                    }
                }
            }
        }
    }

    private fun isShownCreateRefundBtn(): Boolean{
        var countPendingSegments = 0
        args.issuedSegmentList?.forEach {
            if(it.status == Constants.REFUND_STATUS_PENDING) countPendingSegments++
        }
        return !args.issuedSegmentList.isNullOrEmpty() && args.issuedSegmentList!!.size != countPendingSegments
    }


    fun openChooseTicketRefundFragment() = findNavController().navigate(
        RefundListFragmentDirections.actionRefundListFragmentToChooseTicketRefundFragment(
            args.issuedSegmentList!!))

    override fun onDetailClick(refundAppItem: RefundAppItem) {
        openRefundDetailFragment(refundAppItem)
    }

    override fun onCancelClick(refundId: Int) {
        openCancelRefundDialog(refundId)
    }

    private fun openCancelRefundDialog(refundId: Int) = findNavController().navigate(
        RefundListFragmentDirections.actionRefundListFragmentToCancelRefundDialog(refundId))

    private fun openRefundDetailFragment(refundAppItem: RefundAppItem) {
        findNavController().navigate(
            RefundListFragmentDirections.actionRefundListFragmentToRefundDetailFragment(refundAppItem))
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}