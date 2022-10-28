package kz.divtech.odyssey.rotation.ui.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.databinding.FragmentActiveTripsBinding

class ActiveTripsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentActiveTripsBinding.inflate(inflater)

        val tripsList = mutableListOf(Trip("На вахту, 25 авг", "на 10 дней, в Актогай"),
            Trip("Домой, 6 сен", "в Алматы"),
            Trip("На вахту, 17 сен", "на 10 дней, в Актогай", false),
            Trip("Домой,  27 сен", "В Алматы", false))
        binding.activeTripsRV.adapter = TripsAdapter(tripsList) { trip ->
            val action = TripsFragmentDirections.actionTripsFragmentToTripDetailDialog(trip)
            findNavController().navigate(action)
        }

        return binding.root
    }

}