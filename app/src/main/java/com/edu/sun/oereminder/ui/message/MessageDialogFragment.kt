package com.edu.sun.oereminder.ui.message

import android.os.Bundle
import android.view.Gravity.BOTTOM
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.transition.Slide
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.data.model.Message.Companion.ACTUAL_CONTENT_REGEX
import com.edu.sun.oereminder.data.model.Message.Companion.ISSUE_CONTENT_REGEX
import com.edu.sun.oereminder.data.model.Message.Companion.NEXT_CONTENT_REGEX
import com.edu.sun.oereminder.data.model.Message.Companion.PLAN_CONTENT_REGEX
import com.edu.sun.oereminder.data.model.Message.Companion.REPORT_PLAN_REGEX
import com.edu.sun.oereminder.ui.message.ConfirmDialog.Companion.CODE_OK
import com.edu.sun.oereminder.ui.message.ConfirmDialog.Companion.KEY_CONFIRM_RESULT
import com.edu.sun.oereminder.ui.message.ConfirmDialog.Companion.KEY_RESULT_CODE
import com.edu.sun.oereminder.utils.beginTransition
import com.edu.sun.oereminder.utils.normalizeEndLine
import com.edu.sun.oereminder.utils.remove
import com.edu.sun.oereminder.utils.toDate
import kotlinx.android.synthetic.main.dialog_message.*
import kotlinx.android.synthetic.main.toolbar_app.*
import java.util.*

class MessageDialogFragment : DialogFragment() {

    private var isPlan = true
    private var isEdit = false
    private var isDelete = false
    private var message: Message? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.run {
            isPlan = getBoolean(IS_PLAN, true)
            isEdit = getBoolean(IS_EDIT, true)
            message = getParcelable(KEY_MESSAGE)
        }
        return inflater.inflate(R.layout.dialog_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initListener()
    }

    private fun setupView() {
        toolbar.run {
            setTitle(R.string.text_daily_report)
            setNavigationIcon(R.drawable.ic_round_close)
            setNavigationOnClickListener { dismiss() }
            inflateMenu(R.menu.menu_send_report)
        }
        switchReport.isClickable = message == null
        toolbar.menu.findItem(R.id.item_delete).isVisible = isEdit
        if (message == null) {
            setupDefaultMessage()
        } else {
            setupMessage()
        }
    }

    private fun setupDefaultMessage() {
        edtMessage.setText(if (isPlan) R.string.plan_title_default else R.string.report_title_default)
    }

    private fun setupMessage() {
        message?.let {
            it.messageReport.run {
                val plan = remove(REPORT_PLAN_REGEX).remove(PLAN_CONTENT_REGEX).normalizeEndLine()
                when {
                    isEdit && isPlan -> {
                        edtMessage.setText(it.messageTitle)
                        edtPlan.setText(remove(PLAN_CONTENT_REGEX).normalizeEndLine())
                    }
                    isEdit && !isPlan -> {
                        edtMessage.setText(it.messageTitle)
                        edtPlan.setText(plan)
                        edtActual.setText(remove(ACTUAL_CONTENT_REGEX).normalizeEndLine())
                        edtNext.setText(remove(NEXT_CONTENT_REGEX).normalizeEndLine())
                        edtIssue.setText(remove(ISSUE_CONTENT_REGEX).normalizeEndLine())
                    }
                    !isEdit && !isPlan -> {
                        edtMessage.setText(R.string.report_title_default)
                        edtPlan.setText(plan)
                        edtActual.setText(plan)
                    }
                    else -> {
                        edtMessage.setText(R.string.plan_title_default)
                        edtPlan.setText(remove(NEXT_CONTENT_REGEX).normalizeEndLine())
                    }
                }
            }
        }
    }

    private fun initListener() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_send -> setResult()
                R.id.item_delete -> {
                    ConfirmDialog().show(parentFragmentManager, ConfirmDialog::class.simpleName)
                    true
                }
                else -> false
            }
        }
        setFragmentResultListener(KEY_CONFIRM_RESULT) { _, bundle ->
            if (bundle.getInt(KEY_RESULT_CODE) == CODE_OK) {
                isDelete = true
                setResult()
            }
        }
        switchReport.setOnCheckedChangeListener { _, isCheck ->
            handleSwitchChecked(isCheck)
        }
        switchReport.isChecked = !isPlan
    }

    private fun handleSwitchChecked(isCheck: Boolean) {
        linearLayout.beginTransition(
            Slide(BOTTOM),
            R.id.layoutActual,
            R.id.layoutNext,
            R.id.layoutIssue
        )
        textLabelPlan.isVisible = isCheck
        textLabelReport.isVisible = !isCheck
        layoutActual.isVisible = isCheck
        layoutNext.isVisible = isCheck
        layoutIssue.isVisible = isCheck
        if (message == null) {
            isPlan = !isCheck
            setupDefaultMessage()
        }
    }

    private fun setResult(): Boolean {
        val code = when {
            isDelete -> CODE_DELETE_REPORT
            isEdit -> CODE_EDIT_REPORT
            else -> CODE_NEW_REPORT
        }
        setFragmentResult(
            REQUEST_SEND,
            bundleOf(REQUEST_SEND to code, KEY_MESSAGE to getMessageBody())
        )
        dismiss()
        return true
    }

    private fun getMessageBody() = if (isPlan) {
        getString(
            R.string.plan_format,
            edtMessage.text,
            if (isEdit) {
                message?.sendCalendar?.toDate(REPORT_DATE_FORMAT)
            } else {
                GregorianCalendar().toDate(REPORT_DATE_FORMAT)
            },
            edtPlan.text
        )
    } else {
        getString(
            R.string.report_format,
            edtMessage.text,
            edtPlan.text,
            edtActual.text,
            edtNext.text,
            edtIssue.text
        )
    }

    override fun getTheme() = R.style.MessageDialogStyle

    companion object {
        const val REPORT_DATE_FORMAT = "MM/dd/yyyy"
        const val IS_PLAN = "is_plan"
        const val IS_EDIT = "is_edit"
        const val KEY_MESSAGE = "key_message"
        const val REQUEST_SEND = "request_send"
        const val CODE_NEW_REPORT = 0
        const val CODE_EDIT_REPORT = 1
        const val CODE_DELETE_REPORT = 2

        fun newInstance(isPlan: Boolean, isEdit: Boolean, message: Message?) =
            MessageDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_PLAN, isPlan)
                    putBoolean(IS_EDIT, isEdit)
                    putParcelable(KEY_MESSAGE, message)
                }
            }
    }
}
