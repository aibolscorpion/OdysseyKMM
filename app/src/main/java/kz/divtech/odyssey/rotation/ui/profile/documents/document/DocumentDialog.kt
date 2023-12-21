package kz.divtech.odyssey.rotation.ui.profile.documents.document

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.data.remote.result.isFailure
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.databinding.DialogIdBinding
import kz.divtech.odyssey.rotation.databinding.DialogPassportBinding
import kz.divtech.odyssey.rotation.databinding.DialogRecidencyPermitBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Document
import kz.divtech.odyssey.rotation.domain.model.errors.ValidationErrorResponse
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess

@AndroidEntryPoint
class DocumentDialog : BottomSheetDialogFragment() {
    private val args: DocumentDialogArgs by navArgs()
    val viewModel : DocumentViewModel by viewModels()
    private var docNumberET: EditText? = null
    private var issuedByET: EditText? = null
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val document = args.document.copy()

        when(document.type){
            Constants.ID_CARD -> {
                val idBinding = DialogIdBinding.inflate(inflater)
                idBinding.documentDialog = this
                idBinding.document = document
                idBinding.employee = args.employee
                idBinding.viewModel = viewModel
                docNumberET = idBinding.documentLayout.docNumberET
                issuedByET = idBinding.documentLayout.issuedByET
                return idBinding.root
            }

            Constants.PASSPORT -> {
                val passportBinding = DialogPassportBinding.inflate(inflater)
                passportBinding.documentDialog = this
                passportBinding.document = document
                passportBinding.employee = args.employee
                passportBinding.viewModel = viewModel
                docNumberET = passportBinding.documentLayout.docNumberET
                issuedByET = passportBinding.documentLayout.issuedByET
                return passportBinding.root
            }

            Constants.RESIDENCE -> {
                val residenceBinding = DialogRecidencyPermitBinding.inflate(inflater)
                residenceBinding.documentDialog = this
                residenceBinding.document = document
                residenceBinding.viewModel = viewModel
                docNumberET = residenceBinding.documentLayout.docNumberET
                issuedByET = residenceBinding.documentLayout.issuedByET
                return residenceBinding.root
            }
            else -> {
                val residenceBinding = DialogRecidencyPermitBinding.inflate(inflater)
                residenceBinding.documentDialog = this
                residenceBinding.document = document
                residenceBinding.viewModel = viewModel
                docNumberET = residenceBinding.documentLayout.docNumberET
                issuedByET = residenceBinding.documentLayout.issuedByET
                return residenceBinding.root
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateDocumentResult.observe(viewLifecycleOwner){ response ->
            if(response.isSuccess()){
                Toast.makeText(requireContext(), R.string.data_was_successfully_updated, Toast.LENGTH_LONG).show()
                dismiss()
            }else if(response.isHttpException() && (response.statusCode == Constants.UNPROCESSABLE_ENTITY_CODE)){
                val errorResponse =
                    Gson().fromJson(response.error.errorBody?.string(), ValidationErrorResponse::class.java)
                errorResponse.errors.forEach{ (field, errorMessages) ->
                    val firstErrorMessage = errorMessages.first()
                    when(field){
                        "number" -> docNumberET?.let { it.error = firstErrorMessage}
                        "issue_by" -> issuedByET?.let { it.error = firstErrorMessage}
                    }
                }
            }else if(response.isFailure()){
                Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateDocument(document: Document){
        if(requireContext().isNetworkAvailable()){
            if(validateDocument()){
                viewModel.updateDocument(document)
            }
        }else{
            showNoInternetDialog()
        }
    }

    private fun validateDocument(): Boolean{
        var isValid = true
        docNumberET?.let{
            if(it.text.isEmpty()){
                it.error = getString(R.string.fill_document_number_field)
                isValid = false
            }
        }
        return isValid
    }

    private fun showNoInternetDialog() =
        findNavController().navigate(DocumentDialogDirections.actionGlobalNoInternetDialog())

    fun close() {
        dismiss()
    }
}