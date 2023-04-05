package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogSortBinding

class SortTripDialog(private val tripSortClicked: OnTripSortClicked,
                     private val sortType: SortTripType) : BottomSheetDialogFragment() {
    override fun getTheme() = R.style.BottomSheetDialogTheme
    override fun getDialog() = BottomSheetDialog(requireContext(), theme)

    lateinit var binding: DialogSortBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogSortBinding.inflate(inflater)
        binding.thisDialog = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(sortType){
            SortTripType.BY_DEPARTURE_DATE -> binding.departureDateRB.isChecked = true
            SortTripType.BY_STATUS -> binding.statusRB.isChecked = true
        }

        binding.sortRG.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                binding.departureDateRB.id -> tripSortClicked.onDateClicked()
                binding.statusRB.id -> tripSortClicked.onStatusClicked()
            }
        }
    }

    interface OnTripSortClicked{
        fun onDateClicked()
        fun onStatusClicked()
    }
}

enum class SortTripType{
    BY_DEPARTURE_DATE, BY_STATUS
}