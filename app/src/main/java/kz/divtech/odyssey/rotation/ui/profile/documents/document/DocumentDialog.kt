package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.DialogIdBinding
import kz.divtech.odyssey.rotation.databinding.DialogPassportBinding
import kz.divtech.odyssey.rotation.databinding.DialogRecidencyPermitBinding


class DocumentDialog : BottomSheetDialogFragment() {

    val viewModel : DocumentViewModel by viewModels()
    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val document = DocumentDialogArgs.fromBundle(requireArguments()).document.copy()
        val employee = DocumentDialogArgs.fromBundle(requireArguments()).employee

        when(document.type){
            Constants.ID_CARD -> {
                val idBinding = DialogIdBinding.inflate(inflater)
                idBinding.documentDialog = this
                idBinding.document = document
                idBinding.employee = employee
                idBinding.viewModel = viewModel
                return idBinding.root
            }

            Constants.PASSPORT -> {
                val passportBinding = DialogPassportBinding.inflate(inflater)
                passportBinding.documentDialog = this
                passportBinding.document = document
                passportBinding.employee = employee
                passportBinding.viewModel = viewModel
                return passportBinding.root
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