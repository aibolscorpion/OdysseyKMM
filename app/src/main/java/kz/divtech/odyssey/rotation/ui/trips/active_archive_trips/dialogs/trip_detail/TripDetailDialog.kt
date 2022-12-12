package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail

import android.app.Dialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.DialogTripDetailBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters.DownloadTicketButtonAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters.SegmentFullAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters.TicketPriceAdapter
import java.io.File


class TripDetailDialog : BottomSheetDialogFragment(), OnCloseListener , DownloadTicketButtonAdapter.DownloadInterface {
    private val DOWNLOAD_TICKET_LENGTH = 10000
    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme
    private val args: TripDetailDialogArgs by navArgs()
    internal val viewModel: TripDetailViewModel by viewModels()
    private lateinit var dataBinding: DialogTripDetailBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        dataBinding  = DialogTripDetailBinding.inflate(inflater)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().registerReceiver(pdfDownloadedReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        viewModel.setCityAndTotalTimeInWay(args.trip)

        dataBinding.listener = this
        dataBinding.trip = args.trip
        dataBinding.viewModel = viewModel

        setSegmentFullRV()
        setTicketPriceRV()
        setDownloadButtonRV()

        dataBinding.trip
        }

    private fun setSegmentFullRV(){
        val tripDetailAdapter = SegmentFullAdapter()
        tripDetailAdapter.setSegmentList(args.trip.segments)
        dataBinding.segmentDetailRV.adapter = tripDetailAdapter
    }

    private fun setTicketPriceRV(){
        val priceAdapter = TicketPriceAdapter()
        val ticketList = mutableListOf<Ticket>()
        var totalPrice = 0
        var hasIssuedTicket = false
        args.trip.segments?.forEach{ segment ->
            if(segment.status.equals(Constants.STATUS_ISSUED)){
                hasIssuedTicket = true
                val ticket = segment.ticket
                totalPrice += ticket?.sum!!
                ticketList.add(ticket)
            }
        }
        priceAdapter.setTicketPriceList(ticketList)
        dataBinding.ticketPriceCL.visibility = if(hasIssuedTicket) View.VISIBLE else View.GONE
        dataBinding.ticketsPriceRV.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        dataBinding.ticketsPriceRV.adapter = priceAdapter
        dataBinding.totalPriceValueTV.text = requireContext().getString(R.string.ticket_price, totalPrice)
    }

    private fun setDownloadButtonRV(){
        val ticketAdapter = DownloadTicketButtonAdapter(this)
        val ticketList = mutableListOf<Ticket>()
        args.trip.segments?.forEach{ segment ->
            if(segment.status.equals(Constants.STATUS_ISSUED)){
                segment.ticket?.let { ticketList.add(it) }
            }
        }
        ticketAdapter.setTicketList(ticketList)
        dataBinding.ticketsRV.adapter = ticketAdapter
    }


    override fun onDestroy() {
        super.onDestroy()

        requireActivity().unregisterReceiver(pdfDownloadedReceiver)
    }

    override fun close(){
        dismiss()
    }

    private val pdfDownloadedReceiver =  object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(viewModel.downloadId == id){
                Snackbar.make(dataBinding.coordinatorLayout, R.string.open_downloaded_ticket, DOWNLOAD_TICKET_LENGTH)
                .setAction(android.R.string.ok) {
                    openFile()
                }
                .show()

            }
        }
    }

    fun openFile() {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            viewModel.filePathAfterDownloads!!)
        if(file.exists()){
            val fileURI = FileProvider.getUriForFile(App.appContext, App.appContext.packageName+
                    ".provider", file)
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
        }else{
            Toast.makeText(requireContext(), R.string.ticket_not_found, Toast.LENGTH_LONG).show()
        }
    }


    override fun onTicketClicked(currentTicket: Ticket) {
        viewModel.downloadTicket(currentTicket)
    }

}