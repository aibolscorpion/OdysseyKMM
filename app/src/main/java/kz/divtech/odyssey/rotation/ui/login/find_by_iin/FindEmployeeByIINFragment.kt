package kz.divtech.odyssey.rotation.ui.login.find_by_iin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.databinding.FragmentFindEmployeeByIinBinding
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeShort
import kz.divtech.odyssey.rotation.utils.InputUtils.showErrorMessage

class FindEmployeeByIINFragment : Fragment() {
    private val viewModel: FindEmployeeByIINViewModel by viewModels{
        FindEmployeeByIINViewModel.FindEmployeeByIINViewModelFactory(
            (activity?.application as App).findEmployeeRepository)
    }
    private val args: FindEmployeeByIINFragmentArgs by navArgs()
    private var _binding: FragmentFindEmployeeByIinBinding? = null
    private val binding get() = _binding!!
    private lateinit var iin: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFindEmployeeByIinBinding.inflate(inflater)
        binding.iinFragment = this
        binding.phoneNumber = args.phoneNumber
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.employeeData.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { employee ->
                openUpdatePhoneNumber(employee)
            }
        }
        viewModel.isEmployeeNotFounded.observe(viewLifecycleOwner){
            it?.getContentIfNotHandled()?.let { isEmployeeFounded ->
                if(isEmployeeFounded) showEmployeeNotFoundDialog()
            }
        }

        viewModel.employeeData
    }

    fun loginByIIN(){
        iin = binding.iinET.text.toString()
        if(iin.length == Config.IIN_LENGTH)
            viewModel.findByIIN(iin)
        else
            showErrorMessage(requireContext(), binding.searchByIINFL, getString(R.string.enter_iin_fully))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun openUpdatePhoneNumber(employee: EmployeeShort) =
        findNavController().navigate(FindEmployeeByIINFragmentDirections.actionIINFragmentToUpdatePhoneNumber(employee))

    private fun showEmployeeNotFoundDialog() {
        with(findNavController()){
            if(R.id.IINFragment == currentDestination?.id){
                navigate(FindEmployeeByIINFragmentDirections.actionIINFragmentToEmployeeNotFoundDialog(iin))
            }
        }
    }

    fun backToSendSmsFragment() = findNavController().popBackStack()

}