package kz.divtech.odyssey.rotation.ui.trips.refund.application.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentRefundListBinding
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.rotation.ui.trips.refund.application.RefundViewModel

@AndroidEntryPoint
class RefundListFragment : Fragment(), RefundListAdapter.RefundBtnClick {
    private var _binding: FragmentRefundListBinding? = null
    private val binding get() = _binding!!
    private val args: RefundListFragmentArgs by navArgs()
    val adapter : RefundListAdapter by lazy { RefundListAdapter(this) }
    val viewModel: RefundViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRefundListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.thisDialog = this
        val refundAppList = args.trip.refund_applications
        assignPendingStatusToIssuedSegments(refundAppList)
        binding.createRefundAppTV.isVisible = isShownCreateRefundBtn()
        adapter.setRefundList(getRefundListWithRealSegment(refundAppList))
        binding.refundListRV.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.refund_application_list))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "RefundListFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    private fun getRefundListWithRealSegment(refundAppList: List<RefundAppItem>) : List<RefundAppItem> {
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
            args.issuedSegmentList!!, args.trip.id))

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