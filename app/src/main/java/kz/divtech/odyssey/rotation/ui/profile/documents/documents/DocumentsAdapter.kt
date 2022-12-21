package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.databinding.ItemDocumentBinding
import kz.divtech.odyssey.rotation.domain.model.profile.documents.Document
import kz.divtech.odyssey.rotation.utils.Utils

class DocumentsAdapter(val listener: DocumentClickListener) :
    RecyclerView.Adapter<DocumentsAdapter.DocumentViewHolder>() {
    private val documentList = mutableListOf<Document>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = ItemDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bind(documentList[position])
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    fun setDocumentList(listOfDocuments: List<Document>){
        documentList.clear()
        documentList.addAll(listOfDocuments)
        notifyDataSetChanged()
    }

    inner class DocumentViewHolder(val binding: ItemDocumentBinding) : RecyclerView.ViewHolder(binding.root){
        val context: Context = binding.root.context
        private var currentDocument: Document? = null
        private val documentNames = mutableMapOf(
            Constants.ID_CARD to context.getString(R.string.id),
            Constants.PASSPORT to context.getString(R.string.passport),
            Constants.RESIDENCE to context.getString(R.string.residency_permit))
        init {
            binding.root.setOnClickListener{
                listener.onDocumentClicked(currentDocument!!)
            }
        }
        fun bind(document: Document){
            currentDocument = document
            binding.document = currentDocument

            binding.documentNameTV.text = documentNames[document.type]
            val issueDate = Utils.formatByGivenPattern(document.issue_date, Utils.DAY_MONTH_YEAR_PATTERN)
            val expireDate = Utils.formatByGivenPattern(document.expire_date, Utils.DAY_MONTH_YEAR_PATTERN)
            binding.expireDateValueTV.text = context.getString(R.string.dep_arrival_station_name,
                issueDate, expireDate)
        }
    }

    interface DocumentClickListener{
        fun onDocumentClicked(document : Document)
    }
}