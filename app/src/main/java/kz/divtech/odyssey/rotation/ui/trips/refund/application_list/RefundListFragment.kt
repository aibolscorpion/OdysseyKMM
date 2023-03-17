package kz.divtech.odyssey.rotation.ui.trips.refund.application_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentRefundListBinding

class RefundListFragment : Fragment(), RefundListAdapter.RefundBtnClick{
    private var _binding: FragmentRefundListBinding? = null
    private val binding get() = _binding!!
    val adapter : RefundListAdapter by lazy { RefundListAdapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRefundListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.thisDialog = this
        binding.refundListRV.adapter = adapter
    }

    fun openChooseTicketRefundFragment() = findNavController().navigate(
        RefundListFragmentDirections.actionRefundListFragmentToChooseTicketRefundFragment())

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onDetailClick(position: Int) {
        openRefundDetailFragment()
    }

    override fun onCancelClick(position: Int) {
        openCancelRefundDialog()
    }

    private fun openCancelRefundDialog() = findNavController().navigate(
        RefundListFragmentDirections.actionRefundListFragmentToCancelRefundDialog())

    private fun openRefundDetailFragment() = findNavController().navigate(
        RefundListFragmentDirections.actionRefundListFragmentToRefundDetailFragment())

}