package kz.divtech.odyssey.rotation.ui.trips

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener


class TripDetailDialog : BottomSheetDialogFragment(), OnCloseListener{
    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val dataBinding  = kz.divtech.odyssey.rotation.databinding.DialogTripDetailBinding.inflate(inflater)
        dataBinding.listener = this

        val args = TripDetailDialogArgs.fromBundle(requireArguments())
        val trip = args.trip
        dataBinding.trip = trip
        return dataBinding.root
    }

    override fun close(){
        dismiss()
    }

}