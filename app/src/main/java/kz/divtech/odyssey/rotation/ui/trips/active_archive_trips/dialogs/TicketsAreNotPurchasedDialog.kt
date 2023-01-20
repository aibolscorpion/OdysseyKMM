package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogTicketsAreNotPurchasedBinding


class TicketsAreNotPurchasedDialog : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding  = DialogTicketsAreNotPurchasedBinding.inflate(inflater)
        dataBinding.thisDialog = this

        val args = TicketsAreNotPurchasedDialogArgs.fromBundle(requireArguments())
        dataBinding.trip = args.trip

        return dataBinding.root
    }

}