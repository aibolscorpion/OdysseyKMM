package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.FragmentFilterBinding
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.ActiveTripsViewModel

class FilterTripDialog(private val filterClicked: OnFilterClicked): BottomSheetDialogFragment() {
    lateinit var binding : FragmentFilterBinding
    lateinit var viewModel: ActiveTripsViewModel
    private val allStatusList = listOf(Constants.STATUS_ISSUED, Constants.STATUS_RETURNED,
        Constants.STATUS_OPENED, Constants.STATUS_PARTLY)

    override fun getTheme() = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentFilterBinding.inflate(inflater)
        binding.thisDialog = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ActiveTripsViewModel.TripsViewModelFactory((activity as MainActivity).tripsRepository)
        viewModel = ViewModelProvider(requireParentFragment(), factory)[ActiveTripsViewModel::class.java]

        setCheckedStatusRB()
        setCheckedDirection()
    }


    private fun setCheckedStatusRB(){
        if(viewModel.checkedStatusList.containsAll(allStatusList)){
            uncheckStatusCheckBoxes()
        }else{
            viewModel.checkedStatusList.forEach {
                when(it){
                    Constants.STATUS_ISSUED -> binding.issuedCB.isChecked = true
                    Constants.STATUS_RETURNED -> binding.returnedCB.isChecked = true
                    Constants.STATUS_OPENED -> binding.openedCB.isChecked = true
                    Constants.STATUS_PARTLY -> binding.partlyCB.isChecked = true
                }
            }
        }

    }

    private fun setCheckedDirection(){
        if(viewModel.directionList.containsAll(listOf(Constants.TO_WORK, Constants.TO_HOME)) ||
            viewModel.directionList.isEmpty()){
            binding.bothDirectionRB.isChecked = true
        }else if(viewModel.directionList.contains(Constants.TO_HOME)){
            binding.toHomeRB.isChecked = true
        }else if(viewModel.directionList.contains(Constants.TO_WORK)){
            binding.toWorkRB.isChecked = true
        }
    }

    fun applyFilter(){
        viewModel.appliedFilterCount = 0
        getCheckedStatusRB()
        getCheckedDirection()
        filterClicked.applyFilterClicked()
        dismiss()
    }

    private fun getCheckedStatusRB(){
        viewModel.checkedStatusList.clear()

        if(binding.issuedCB.isChecked){
            viewModel.checkedStatusList.add(Constants.STATUS_ISSUED)
        }
        if(binding.returnedCB.isChecked){
            viewModel.checkedStatusList.add(Constants.STATUS_RETURNED)
        }
        if(binding.openedCB.isChecked){
            viewModel.checkedStatusList.add(Constants.STATUS_OPENED)
        }
        if(binding.partlyCB.isChecked){
            viewModel.checkedStatusList.add(Constants.STATUS_PARTLY)
        }

        if(viewModel.checkedStatusList.isNotEmpty() &&
                        viewModel.checkedStatusList.size != allStatusList.size){
            viewModel.appliedFilterCount++
        }else{
            viewModel.checkedStatusList.addAll(allStatusList)
        }

    }

    private fun getCheckedDirection(){
        viewModel.directionList.clear()
        when(binding.directionRG.checkedRadioButtonId){
            R.id.bothDirectionRB -> {
                viewModel.directionList.add(Constants.TO_HOME)
                viewModel.directionList.add(Constants.TO_WORK)
            }
            R.id.toHomeRB -> {
                viewModel.appliedFilterCount++
                viewModel.directionList.add(Constants.TO_HOME)
            }
            R.id.toWorkRB -> {
                viewModel.appliedFilterCount++
                viewModel.directionList.add(Constants.TO_WORK)
            }
        }

    }

    private fun uncheckStatusCheckBoxes(){
        val listOfCheckBox = listOf(binding.issuedCB, binding.returnedCB,
            binding.openedCB, binding.partlyCB)

        listOfCheckBox.forEach {
            it.isChecked = false
        }
    }

    fun resetFilter(){
        uncheckStatusCheckBoxes()
        binding.bothDirectionRB.isChecked = true
    }

}

interface OnFilterClicked{
    fun applyFilterClicked()
}