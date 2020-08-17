package com.edu.sun.oereminder.ui.settings

import android.annotation.SuppressLint
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.edu.sun.oereminder.AppExecutors
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.utils.trimQuotationMark
import kotlinx.android.synthetic.main.dialog_chatwork.*
import kotlinx.android.synthetic.main.toolbar_app.*

class ChatWorkDialog : DialogFragment() {

    private var clipboard: ClipboardManager? = null

    override fun getTheme() = R.style.MessageDialogStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_chatwork, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        toolbar.run {
            inflateMenu(R.menu.menu_token)
            setTitle(R.string.title_edit_token)
            setNavigationIcon(R.drawable.ic_round_close)
            setNavigationOnClickListener {
                setFragmentResult(REQUEST_TOKEN_RESULT, bundleOf(RESULT_CODE to CODE_CANCEL))
                dismiss()
            }
        }
        edtToken.setText(arguments?.getString(RESULT_TOKEN))
        initListener()
    }

    private fun initListener() {
        inputLayout.setEndIconOnClickListener {
            clipboard?.run {
                if (hasPrimaryClip() && primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) == true) {
                    primaryClip?.run { edtToken.setText(getItemAt(0).text) }
                }
            }
        }
        toolbar.setOnMenuItemClickListener {
            if (it.itemId != R.id.item_save) true else {
                val token = edtToken.text.toString()
                if (token.isNotBlank()) {
                    setFragmentResult(
                        REQUEST_TOKEN_RESULT,
                        bundleOf(RESULT_CODE to CODE_OK, RESULT_TOKEN to token)
                    )
                    dismiss()
                } else {
                    inputLayout.error = getString(R.string.msg_required_field)
                }
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupWebView()
    }

    override fun onPause() {
        webViewChatWork?.loadUrl(URL_BLANK)
        super.onPause()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webViewChatWork.run {
            var loadToken = false
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    when (url) {
                        BASE_URL -> {
                            loadUrl(URL_TOKEN)
                            loadToken = false
                        }
                        URL_TOKEN -> {
                            if (loadToken) {
                                evaluateJavascript(SCRIPT_TOKEN) {
                                    AppExecutors.getInstance().mainThread.execute {
                                        edtToken.setText(it.trimQuotationMark())
                                    }
                                }
                            } else {
                                loadToken = true
                            }
                        }
                    }
                }
            }
            loadUrl(URL_LOGIN)
        }
    }

    companion object {
        const val REQUEST_TOKEN_RESULT = "token_fragment_result"
        const val RESULT_CODE = "result_code"
        const val RESULT_TOKEN = "result_token"
        const val CODE_OK = 1
        const val CODE_CANCEL = 0
        const val URL_BLANK = "about:blank"
        const val BASE_URL = "https://www.chatwork.com/"
        const val URL_LOGIN = "${BASE_URL}login.php"
        const val URL_TOKEN = "${BASE_URL}service/packages/chatwork/subpackages/api/token.php"
        const val SCRIPT_TOKEN = "(() => document.getElementById('token').value)();"

        fun newInstance(token: String) = ChatWorkDialog().apply {
            arguments = bundleOf(RESULT_TOKEN to token)
        }
    }
}
