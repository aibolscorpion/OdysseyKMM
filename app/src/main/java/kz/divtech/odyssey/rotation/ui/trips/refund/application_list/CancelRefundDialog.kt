package kz.divtech.odyssey.rotation.ui.trips.refund.application_list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.DialogCancelRefundBinding

class CancelRefundDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = DialogCancelRefundBinding.inflate(inflater)
        binding.thisDialog = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }


    fun openRefundCancelledFragment(){
        findNavController().navigate(CancelRefundDialogDirections.
            actionCancelRefundDialogToRefundSentFragment(false, "№1000"))
    }



}