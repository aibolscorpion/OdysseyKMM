package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.open_pdf

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.databinding.FragmentChooseTicketForOpenBinding
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Ticket
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.TripDetailDialogDirections
import kz.divtech.odyssey.rotation.common.utils.TicketDownloadUtil.getFile
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable
import kz.divtech.odyssey.rotation.common.utils.RecyclerViewUtil.addItemDecorationWithoutLastDivider
import java.io.File

class ChooseTicketForOpen : Fragment(), DownloadInterface {
    private var _binding: FragmentChooseTicketForOpenBinding? = null
    private val binding get() = _binding!!
    val adapter by lazy { TicketNameRecyclerViewAdapter(this, requireContext()) }
    val args: ChooseTicketForOpenArgs by navArgs()
    val viewModel: OpenTicketViewModel by viewModels()

    private val pdfDownloadedReceiver =  object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(viewModel.downloadIds.contains(id)){
                val ticket = viewModel.tickets[id]
                val position = args.issuedTickets.indexOf(ticket)
                adapter.notifyItemChanged(position)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentChooseTicketForOpenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context?.registerReceiver(pdfDownloadedReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED)
        }else{
            context?.registerReceiver(pdfDownloadedReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }

        binding.chooseTicketForOpenRV.adapter = adapter
        adapter.setTicketList(args.issuedTickets.toList())
        binding.chooseTicketForOpenRV.addItemDecorationWithoutLastDivider()
    }

    private fun openFile(file: File){
        if(file.exists()){
            val fileURI = FileProvider.getUriForFile(
                App.appContext, App.appContext.packageName+ ".provider", file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setDataAndType(fileURI, "application/pdf")
            try{
                startActivity(intent)
            }catch (e: ActivityNotFoundException){
                Toast.makeText(requireContext(), R.string.no_app_for_view_pdf, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onTicketClicked(currentTicket: Ticket) {
        val file = currentTicket.getFile()

        if(file.exists()){
            openFile(file)
        }else{
            if(requireContext().isNetworkAvailable()){
                viewModel.downloadTicketByURL(currentTicket)
            }else{
                showErrorDialog()
            }
        }
    }

    private fun showErrorDialog() =
        findNavController().navigate(TripDetailDialogDirections.actionGlobalNoInternetDialog())


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()

        requireActivity().unregisterReceiver(pdfDownloadedReceiver)
    }

}