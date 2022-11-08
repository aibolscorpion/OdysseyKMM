package kz.divtech.odyssey.rotation.ui.login.search_by_iin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.databinding.FragmentSearchByIinBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee

class SearchIINFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[SearchIINViewModel::class.java] }
    private lateinit var binding: FragmentSearchByIinBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchByIinBinding.inflate(inflater)
        binding.iinFragment = this

        val args = SearchIINFragmentArgs.fromBundle(requireArguments())
        binding.phoneNumber = args.phoneNumber
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.employeeData.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { employee ->
                if(employee.isPhoneNumber!!) openChangePhoneNumberFragment(employee) else openAddPhoneNumberFragment(employee)
            }
        }
    }

    private fun openAddPhoneNumberFragment(employee: Employee){
        findNavController().navigate(SearchIINFragmentDirections.actionIINFragmentToAddPhoneNumber(employee))
    }

    private fun openChangePhoneNumberFragment(employee: Employee){
        findNavController().navigate(SearchIINFragmentDirections.actionIINFragmentToChangeNumberFragment(employee))
    }

    private fun showEmployeeNotFoundDialog(iin: String){
        findNavController().navigate(SearchIINFragmentDirections.actionIINFragmentToEmployeeNotFoundDialog(iin))
    }

    fun backToSendSmsFragment(){
        findNavController().popBackStack()
    }

    fun loginByIIN(){
        val iinEditText = binding.iinET
        val iin = iinEditText.text.toString()
        if(iin.length == Config.IIN_LENGTH)
            viewModel.searchByIIN(iin)
        else
            Toast.makeText(requireContext(), R.string.fill_all_empty_fields, Toast.LENGTH_SHORT).show()
    }

}