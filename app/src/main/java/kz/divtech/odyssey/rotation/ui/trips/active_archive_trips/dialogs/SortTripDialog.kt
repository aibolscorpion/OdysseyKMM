package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.databinding.DialogSortBinding

class SortTripDialog : BottomSheetDialogFragment() {

    override fun getDialog(): Dialog? {
        return super.getDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogSortBinding.inflate(inflater)
        binding.thisDialog = this
        return binding.root
    }
}