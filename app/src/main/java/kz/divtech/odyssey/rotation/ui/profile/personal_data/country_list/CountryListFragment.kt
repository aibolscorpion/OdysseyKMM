package kz.divtech.odyssey.rotation.ui.profile.personal_data.country_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.databinding.FragmentCountryListBinding
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.ui.profile.personal_data.PersonalDataViewModel

class CountryListFragment : Fragment(){
    var _binding: FragmentCountryListBinding? = null
    val binding get() = _binding!!
    val viewModel : PersonalDataViewModel by activityViewModels{
        PersonalDataViewModel.PersonalDataViewModelFactory((activity as MainActivity).employeeRepository)
    }
    val args: CountryListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCountryListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CountryListAdapter()
        binding.newsRecyclerView.adapter = adapter
        adapter.setCountryList(args.countryList.toList(), args.countryCode)

        binding.chooseCountryBtn.setOnClickListener {
            adapter.country?.let { it1 ->
                viewModel.setCountry(it1)
            }
            findNavController().popBackStack()
        }

        binding.countrySearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filteredCountries = args.countryList.filter {
                    it.name.contains(newText, ignoreCase = true)
                }
                adapter.setCountryList(filteredCountries, args.countryCode)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}