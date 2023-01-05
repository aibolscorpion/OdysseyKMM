package kz.divtech.odyssey.rotation.ui.help.press_service.article

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.databinding.DialogArticleBinding
import kz.divtech.odyssey.rotation.ui.login.listener.OnCloseListener


class ArticleDialog : BottomSheetDialogFragment(), OnCloseListener {
    val viewModel: ArticleViewModel by viewModels()
    lateinit var dataBinding: DialogArticleBinding
    override fun getTheme(): Int = R.style.TermsOfAgreementBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        dataBinding  = DialogArticleBinding.inflate(inflater)
        dataBinding.listener = this
        dataBinding.viewModel = viewModel

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fullArticleLiveData.observe(viewLifecycleOwner){ fullArticle ->
            dataBinding.fullArticle = fullArticle
        }

        val args = ArticleDialogArgs.fromBundle(requireArguments())
        viewModel.getArticleById(args.articleId)

    }

    override fun close(){
        dismiss()
    }

}