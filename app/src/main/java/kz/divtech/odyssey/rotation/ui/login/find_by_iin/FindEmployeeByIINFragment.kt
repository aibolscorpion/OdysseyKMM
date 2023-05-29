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
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.databinding.FragmentFindEmployeeByIinBinding
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeShort
import kz.divtech.odyssey.rotation.utils.InputUtils.showErrorMessage
import kz.divtech.odyssey.rotation.utils.NetworkUtils.isNetworkAvailable

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

        viewModel.employeeResult.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { result ->
                if(result.isSuccess()){
                    val employeeResult = result.asSuccess().value
                    if(employeeResult.exists){
                        val employee = employeeResult.employee
                        openUpdatePhoneNumber(employee)
                    }else{
                        showEmployeeNotFoundDialog()
                    }
                }else{
                    showErrorMessage(requireContext(), binding.searchByIINFL, "$result")
                }
            }
        }

        viewModel.employeeResult
    }

    fun loginByIIN(){
        iin = binding.iinET.text.toString()
        if(requireContext().isNetworkAvailable()){
            if(iin.length == Config.IIN_LENGTH)
                viewModel.findByIIN(iin)
            else
                showErrorMessage(requireContext(), binding.searchByIINFL, getString(R.string.enter_iin_fully))
        }else{
            showNoInternetDialog()
        }
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

    private fun showNoInternetDialog(){
        findNavController().navigate(FindEmployeeByIINFragmentDirections.actionGlobalNoInternetDialog2())
    }

    fun backToSendSmsFragment() = findNavController().popBackStack()

}