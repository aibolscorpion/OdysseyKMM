package kz.divtech.odyssey.rotation.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val binding = FragmentHelpBinding.inflate(inflater)
        binding.helpFragment = this

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val bundle = bundleOf()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.help))
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "HelpFragment")
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun writeSupport() = findNavController().navigate(R.id.action_global_writeSupportDialog)

    fun callSupport() = findNavController().navigate(R.id.action_global_callSupportDialog)

    fun openQAFragment() = findNavController().navigate(R.id.action_global_questionsAnswersFragment)

    fun openPressServiceFragment(){
        with(findNavController()){
            if(currentDestination?.id == R.id.helpFragment){
                navigate(HelpFragmentDirections.actionHelpFragmentToNewsFragment())
            }
        }
    }
}