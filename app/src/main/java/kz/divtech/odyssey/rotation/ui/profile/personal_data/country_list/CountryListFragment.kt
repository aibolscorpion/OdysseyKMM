package kz.divtech.odyssey.rotation.ui.profile.personal_data.country_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.databinding.FragmentCountryListBinding

class CountryListFragment : Fragment(){
    var _binding: FragmentCountryListBinding? = null
    val binding get() = _binding!!
    val args: CountryListFragmentArgs by navArgs()
    private val countrySelectionRequestKey = "countrySelectionRequestKey"
    private val countrySelectionResultKey = "countrySelectionResultKey"

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
                val bundle = Bundle().apply {
                    putParcelable(countrySelectionResultKey, it1)
                }

                parentFragmentManager.setFragmentResult(countrySelectionRequestKey, bundle)
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