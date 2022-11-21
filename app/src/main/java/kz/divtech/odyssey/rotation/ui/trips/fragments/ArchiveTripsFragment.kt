package kz.divtech.odyssey.rotation.ui.trips.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.databinding.FragmentArchiveTripsBinding

class ArchiveTripsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentArchiveTripsBinding.inflate(inflater)


        return binding.root
    }
}