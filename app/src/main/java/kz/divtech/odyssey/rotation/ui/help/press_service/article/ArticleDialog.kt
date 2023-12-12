package kz.divtech.odyssey.rotation.ui.help.press_service.article

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.data.remote.result.isFailure
import kz.divtech.odyssey.rotation.databinding.DialogArticleBinding
import kz.divtech.odyssey.rotation.common.utils.NetworkUtils.isNetworkAvailable

@AndroidEntryPoint
class ArticleDialog : BottomSheetDialogFragment() {
    private val args: ArticleDialogArgs by navArgs()
    val viewModel: ArticleViewModel by viewModels()
    private var _dataBinding: DialogArticleBinding? = null
    private val dataBinding get() = _dataBinding!!

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _dataBinding  = DialogArticleBinding.inflate(inflater)
        dataBinding.thisDialog = this
        dataBinding.viewModel = viewModel

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleId = args.articleId

        viewModel.articleResult.observe(viewLifecycleOwner){ result ->
            if(result.isFailure()){
                Toast.makeText(requireContext(), "$result", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getArticleById(articleId).observe(viewLifecycleOwner){ fullArticle ->
            if(fullArticle != null){
                dataBinding.fullArticle = fullArticle
            }else{
                if(requireContext().isNetworkAvailable()){
                    viewModel.getArticleByIdFromServer(articleId)
                    viewModel.markArticleAsRead(articleId)
                }else{
                    showNoInternetDialog()
                }
            }
        }
    }

    private fun showNoInternetDialog(){
        findNavController().navigate(ArticleDialogDirections.actionGlobalNoInternetDialog())
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _dataBinding = null
    }

}