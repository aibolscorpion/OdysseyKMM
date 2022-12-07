package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.DialogTripDetailBinding
import kz.divtech.odyssey.rotation.domain.model.trips.Ticket
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters.DownloadTicketButtonAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters.SegmentFullAdapter
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.trip_detail.adapters.TicketPriceAdapter


class TripDetailDialog : BottomSheetDialogFragment(), OnCloseListener {
    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme
    private val args: TripDetailDialogArgs by navArgs()
    private val viewModel: TripDetailViewModel by viewModels()
    private lateinit var dataBinding: DialogTripDetailBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        dataBinding  = DialogTripDetailBinding.inflate(inflater)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val ticketAdapter = DownloadTicketButtonAdapter()
        val ticketList = mutableListOf<Ticket>()
        args.trip.segments?.forEach{ segment ->
            if(segment.status.equals(Constants.STATUS_ISSUED)){
                segment.ticket?.let { ticketList.add(it) }
            }
        }
        ticketAdapter.setTicketList(ticketList)
        dataBinding.ticketsRV.adapter = ticketAdapter
    }


    override fun close(){
        dismiss()
    }

}