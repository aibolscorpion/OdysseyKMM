package kz.divtech.odyssey.rotation.ui.profile.documents.documents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kz.divtech.odyssey.rotation.databinding.ItemDocumentBinding
import kz.divtech.odyssey.shared.domain.model.profile.Document

class DocumentsAdapter(private val documentListener: DocumentListener) :
        RecyclerView.Adapter<DocumentsAdapter.DocumentViewHolder>() {
    private val userDocumentList = mutableListOf<Document>()
    private val documentList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = ItemDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.listener = documentListener
        return DocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.binding.document = getDocumentByName(documentList[position])
        holder.binding.documentShortInfoCL.isVisible = isDocumentExist(documentList[position])
        holder.binding.noDocumentInfoTV.isVisible = !isDocumentExist(documentList[position])
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    fun setUserDocumentList(userDocuments: List<Document>, documents: List<String>){
        documentList.clear()
        userDocumentList.clear()
        documentList.addAll(documents)
        userDocumentList.addAll(userDocuments)
        notifyDataSetChanged()
    }

    private fun isDocumentExist(documentName: String): Boolean {
        userDocumentList.forEach {
            if(documentName == it.type){
                return true
            }
        }
        return false
    }

    private fun getDocumentByName(documentName: String): Document {
        userDocumentList.forEach {
            if(documentName == it.type){
                return it
            }
        }
        return Document(null, null, null, null, null, documentName, false)
    }
    inner class DocumentViewHolder(val binding: ItemDocumentBinding) : RecyclerView.ViewHolder(binding.root)

    interface DocumentListener{
        fun onDocumentClicked(document : Document)
    }
}