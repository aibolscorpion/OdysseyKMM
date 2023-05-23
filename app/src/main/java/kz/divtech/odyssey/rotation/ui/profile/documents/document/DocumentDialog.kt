package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.DialogIdBinding
import kz.divtech.odyssey.rotation.databinding.DialogPassportBinding
import kz.divtech.odyssey.rotation.databinding.DialogRecidencyPermitBinding
import kz.divtech.odyssey.rotation.ui.MainActivity


class DocumentDialog : BottomSheetDialogFragment() {
    private val args: DocumentDialogArgs by navArgs()
    val viewModel : DocumentViewModel by viewModels{
        DocumentViewModel.DocumentViewModelFactory((activity as MainActivity).employeeRepository)
    }
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val document = args.document.copy()

        viewModel.documentUpdated.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), R.string.data_was_successfully_updated, Toast.LENGTH_LONG).show()
            dismiss()
        }

        when(document.type){
            Constants.ID_CARD -> {
                val idBinding = DialogIdBinding.inflate(inflater)
                idBinding.documentDialog = this
                idBinding.document = document
                idBinding.employee = args.employee
                idBinding.viewModel = viewModel
                return idBinding.root
            }

            Constants.PASSPORT -> {
                val passportBinding = DialogPassportBinding.inflate(inflater)
                passportBinding.documentDialog = this
                passportBinding.document = document
                passportBinding.employee = args.employee
                passportBinding.viewModel = viewModel
                return passportBinding.root
            }

            Constants.RESIDENCE -> {
                val residenceBinding = DialogRecidencyPermitBinding.inflate(inflater)
                residenceBinding.documentDialog = this
                residenceBinding.document = document
                residenceBinding.viewModel = viewModel
                return residenceBinding.root
            }
            else -> {
                val residenceBinding = DialogRecidencyPermitBinding.inflate(inflater)
                residenceBinding.documentDialog = this
                residenceBinding.document = document
                residenceBinding.viewModel = viewModel
                return residenceBinding.root
            }
        }
    }

    fun close() {
        dismiss()
    }
}