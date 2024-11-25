package kz.divtech.odyssey.rotation.ui.trips.refund.application.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentRefundReasonBinding
import kz.divtech.odyssey.rotation.ui.trips.refund.application.RefundViewModel
import kz.divtech.odyssey.rotation.common.utils.KeyboardUtils
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.shared.common.Resource

@AndroidEntryPoint
class RefundReasonFragment: Fragment() {
    private val args : RefundReasonFragmentArgs by navArgs()
    lateinit var binding: FragmentRefundReasonBinding
    val viewModel: RefundViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRefundReasonBinding.inflate(inflater)
        binding.thisDialog = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRadioGroup()

        viewModel.sendRefundResult.observe(viewLifecycleOwner){ result ->
            if(result is Resource.Success){
                result.data?.let {
                    openRefundSendFragment(it.id)
                }
            }else{
                Toast.makeText(requireContext(), "${result.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRadioGroup(){
        binding.reasonsRG.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.userVariantRB -> {
                    KeyboardUtils.showSoftKeyboard(requireContext(), binding.userVariantET)
                }
                else -> {
                    KeyboardUtils.hideSoftKeyboard(requireContext(), binding.userVariantET)
                    binding.userVariantET.clearFocus()
                }
            }
        }

        binding.userVariantET.setOnFocusChangeListener{ _, hasFocus ->
            if(hasFocus){
                binding.userVariantRB.isChecked = true
            }
        }
    }

    fun sendApplicationToRefund(){
        if(getReasonOfRefund().isEmpty()) {
            Toast.makeText(requireContext(), R.string.specify_refund_reason, Toast.LENGTH_SHORT).show()
            return
        }
        if(requireContext().isNetworkAvailable()){
            viewModel.sendApplicationToRefund(args.applicationId, args.segmentId,
                getReasonOfRefund())
        }else{
            showNoInternetDialog()
        }

    }

    private fun getReasonOfRefund(): String{
        val checkedRadioId = binding.reasonsRG.checkedRadioId
        return if(checkedRadioId != -1){
            val checkedRadioBtn = binding.reasonsRG.findViewById<RadioButton>(checkedRadioId)
            if(checkedRadioId == binding.userVariantRB.id) {
                binding.userVariantET.text.toString()
            }else {
                checkedRadioBtn.text.toString()
            }
        }else{
            ""
        }
    }

    private fun showNoInternetDialog(){
        findNavController().navigate(RefundReasonFragmentDirections.actionGlobalNoInternetDialog())
    }

    private fun openRefundSendFragment(refundId: Int) {
        findNavController().navigate(
            RefundReasonFragmentDirections.actionRefundReasonFragmentToRefundSendFragment(true, refundId))
    }

}