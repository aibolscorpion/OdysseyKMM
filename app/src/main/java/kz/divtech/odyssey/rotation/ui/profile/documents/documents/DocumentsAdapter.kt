package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemDocumentBinding
import kz.divtech.odyssey.rotation.domain.model.profile.Document

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
        private var currentDocument: Document? = null
        init {
            binding.root.setOnClickListener{
                listener.onDocumentClicked(currentDocument!!)
            }
        }
        fun bind(document: Document){
            currentDocument = document
            binding.document = currentDocument
        }
    }

    interface DocumentClickListener{
        fun onDocumentClicked(document : Document)
    }
}