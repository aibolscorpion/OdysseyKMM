package kz.divtech.odyssey.rotation.ui.help.press_service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentPressServiceBinding

class PressServiceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentPressServiceBinding.inflate(inflater)


        val listOfNews = mutableListOf(
            News(getString(R.string.news_title_placeholder),getString(R.string.news_content_placeholder), getString(R.string.news_time_placeholder)),
            News("РЖД запускает 300 поездов «Сапсан» в Казахстаан","Новые скоростные поезда будут доступны для пассажирских перевозок Новые скоростные поезда будут доступны для пассажирских перевозок", getString(R.string.news_time_placeholder)),
            News(getString(R.string.news_title_placeholder),getString(R.string.news_content_placeholder), getString(R.string.news_time_placeholder)),
            News(getString(R.string.notification_title_placeholder),getString(R.string.notification_content_placeholder), getString(R.string.notification_time_placeholder)))

        binding.newsRecyclerView.adapter = NewsAdapter(listOfNews) {
            val action = PressServiceFragmentDirections.actionPressServiceFragmentToNewsDialog(it)
            findNavController().navigate(action)
        }

        return binding.root
    }
}