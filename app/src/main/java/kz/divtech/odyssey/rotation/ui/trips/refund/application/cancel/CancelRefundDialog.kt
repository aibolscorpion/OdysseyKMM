package kz.divtech.odyssey.rotation.ui.trips.refund.application.cancel

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.databinding.DialogCancelRefundBinding
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.trips.refund.application.RefundViewModel

class CancelRefundDialog : DialogFragment() {
    val args: CancelRefundDialogArgs by navArgs()
    val viewModel: RefundViewModel by viewModels{
        RefundViewModel.RefundViewModelFactory((activity as MainActivity).refundRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DialogCancelRefundBinding.inflate(inflater)
        binding.thisDialog = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    fun cancelRefund(){
        lifecycleScope.launch{
            val result = viewModel.cancelRefund(args.refundId)
            if(result.isSuccess()){
                openRefundCancelledFragment()
            }else{
                Toast.makeText(requireContext(), R.string.error_happened, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun openRefundCancelledFragment(){
        findNavController().navigate(CancelRefundDialogDirections.
            actionCancelRefundDialogToRefundSentFragment(false, args.refundId))
    }



}