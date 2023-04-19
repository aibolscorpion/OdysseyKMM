package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.databinding.FragmentChooseTicketForOpenBinding

class ChooseTicketForOpen : Fragment() {
    private var _binding: FragmentChooseTicketForOpenBinding? = null
    private val binding get() = _binding!!
    val adapter by lazy { MyItemRecyclerViewAdapter() }
    val args: ChooseTicketForOpenArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentChooseTicketForOpenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chooseTicketForOpenRV.adapter = adapter
        adapter.setTicketList(args.issuedTickets.toList())

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}