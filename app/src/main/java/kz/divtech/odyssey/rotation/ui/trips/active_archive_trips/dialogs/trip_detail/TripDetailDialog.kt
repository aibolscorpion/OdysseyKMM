package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail

import android.app.Dialog
import android.app.DownloadManager
import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.DialogTripDetailBinding
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Segment
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Ticket
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters.SegmentFullAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters.TicketPriceAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.open_pdf.OpenTicketViewModel
import kz.divtech.odyssey.rotation.utils.TicketDownloadUtil.getFile
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils.getLocalDateByPattern
import kz.divtech.odyssey.rotation.utils.NetworkUtils.isNetworkAvailable
import java.io.File
import java.time.LocalDate


class TripDetailDialog : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    private val args: TripDetailDialogArgs by navArgs()
    private lateinit var dataBinding: DialogTripDetailBinding
    val viewModel: OpenTicketViewModel by viewModels()
    var ticketList = mutableListOf<Ticket>()

    private val pdfDownloadedReceiver =  object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(viewModel.downloadIds.contains(id)){
                viewModel.files[id]?.let { openFile(it) }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        dataBinding  = DialogTripDetailBinding.inflate(inflater)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketList.addAll(getTickets().toList())

        dataBinding.thisDialog = this
        dataBinding.trip = args.trip
        dataBinding.showTicketsTV.isVisible = ticketList.isNotEmpty()

        requireActivity().registerReceiver(pdfDownloadedReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        setSegmentFullRV()
        setTicketPriceRV()
        setRefundButtons()
    }

    private fun setSegmentFullRV(){
        val tripDetailAdapter = SegmentFullAdapter(requireContext())
        tripDetailAdapter.setSegmentList(args.trip)
        dataBinding.segmentDetailRV.adapter = tripDetailAdapter
    }

    private fun setTicketPriceRV(){
        val priceAdapter = TicketPriceAdapter()
        val ticketList = mutableListOf<Ticket>()
        var totalPrice = 0
        var hasIssuedTicket = false
        args.trip.segments.forEach{ segment ->
            if(segment.status == Constants.STATUS_ISSUED){
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

    private fun getActiveIssuedSegments() : Array<Segment> {
        val listOfIssuedSegments = mutableListOf<Segment>()
        val date = args.trip.date.getLocalDateByPattern()
        if(date.isAfter(LocalDate.now())){
            args.trip.segments.forEach { segment ->
                if(segment.status == Constants.STATUS_ISSUED){
                    listOfIssuedSegments.add(segment)
                }
            }
        }
        return listOfIssuedSegments.toTypedArray()
    }

    private fun getTickets() : Array<Ticket> {
        val listOfTickets = mutableListOf<Ticket>()
        args.trip.segments.forEach { segment ->
            if(segment.status == Constants.STATUS_ISSUED){
                segment.ticket?.let { listOfTickets.add(it) }
            }
        }
        return listOfTickets.toTypedArray()
    }

    private fun setRefundButtons(){
        val refundApplications = args.trip.refund_applications
        dataBinding.refundCL.isVisible = refundApplications.isNotEmpty()
        dataBinding.refundApplicationSizeTV.text = refundApplications.size.toString()

        dataBinding.createRefundAppTV.isVisible = refundApplications.isEmpty()
                && getActiveIssuedSegments().isNotEmpty()
    }

    internal fun openFile(file: File){
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

    fun openTickets(){
        if(ticketList.size == 1){
            openFileIfExists(ticketList[0])
        }else{
            openChooseTicketForOpenFragment()
        }
    }

    private fun openFileIfExists(ticket: Ticket){
        val file = ticket.getFile()
        if(file.exists()){
            openFile(file)
        }else{
            if(requireContext().isNetworkAvailable()){
                viewModel.downloadTicketByURL(ticket)
            }else{
                showErrorDialog()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        requireActivity().unregisterReceiver(pdfDownloadedReceiver)
    }

    private fun showErrorDialog() =
        findNavController().navigate(TripDetailDialogDirections.actionGlobalNoInternetDialog())


    private fun openChooseTicketForOpenFragment() = findNavController().navigate(
        TripDetailDialogDirections.actionTripDetailDialogToChooseTicketForOpen(ticketList.toTypedArray())
    )

    fun openChooseTicketRefundFragment() = findNavController().navigate(
        TripDetailDialogDirections.actionTripDetailDialogToChooseTicketRefundFragment(getActiveIssuedSegments(), args.trip.id))

    fun openRefundListFragment() = findNavController().navigate(
        TripDetailDialogDirections.actionGlobalRefundListFragment(getActiveIssuedSegments(), args.trip)
    )

}