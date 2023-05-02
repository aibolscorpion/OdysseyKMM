package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemDocumentBinding
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Document

class DocumentsAdapter(private val documentListener: DocumentListener) :
    RecyclerView.Adapter<DocumentsAdapter.DocumentViewHolder>() {
    private val documentList = mutableListOf<Document>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = ItemDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.listener = documentListener
        return DocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.binding.document = documentList[position]
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    fun setDocumentList(listOfDocuments: List<Document>){
        documentList.clear()
        documentList.addAll(listOfDocuments)
        notifyDataSetChanged()
    }

    inner class DocumentViewHolder(val binding: ItemDocumentBinding) : RecyclerView.ViewHolder(binding.root)

    interface DocumentListener{
        fun onDocumentClicked(document : Document)
    }
}