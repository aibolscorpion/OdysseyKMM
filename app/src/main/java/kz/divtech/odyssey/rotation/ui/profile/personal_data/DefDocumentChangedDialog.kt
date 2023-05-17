package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kz.divtech.odyssey.rotation.databinding.DialogDefaultDocumentBinding

class DefDocumentChangedDialog(val listener: UpdatePersonalDataListener) : DialogFragment() {
    var _binding: DialogDefaultDocumentBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding =  DialogDefaultDocumentBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveBtn.setOnClickListener{
            listener.update()
            dismiss()
        }
        binding.cancelBtn.setOnClickListener{
            dismiss()
        }
    }


}
interface UpdatePersonalDataListener{
    fun update()
}