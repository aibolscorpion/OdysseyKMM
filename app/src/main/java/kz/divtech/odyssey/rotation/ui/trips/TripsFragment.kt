package kz.divtech.odyssey.rotation.ui.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentTripsBinding

class TripsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val binding = FragmentTripsBinding.inflate(inflater)

        activity?.findViewById<TextView>(R.id.toolbarTitleTV)?.setText(R.string.trips)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }
}