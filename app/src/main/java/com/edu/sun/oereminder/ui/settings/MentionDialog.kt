package com.edu.sun.oereminder.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.utils.InjectorUtils
import kotlinx.android.synthetic.main.dialog_mention.*
import kotlinx.android.synthetic.main.toolbar_app.*

class MentionDialog : DialogFragment(), MentionContract.View {

    private val memberAdapter by lazy { MemberAdapter() }
    private val presenter by lazy {
        MentionPresenter(InjectorUtils.getUserRepository(requireContext()), this)
    }

    override fun getTheme() = R.style.MessageDialogStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_mention, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initListener()
    }

    private fun setupView() {
        toolbar.run {
            setTitle(R.string.title_mention)
            setNavigationIcon(R.drawable.ic_round_arrow_back)
            inflateMenu(R.menu.menu_mention)
        }
        recyclerMember.adapter = memberAdapter
        presenter.loadScreen()
    }

    private fun initListener() {
        memberAdapter.setOnclickListener { member, index ->
            presenter.onItemClick(member, index)
        }
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.item_save) {
                presenter.onSaveClick()
                dismiss()
                true
            } else false
        }
    }

    override fun updateRecyclerView(data: List<Member>, index: Int?) {
        memberAdapter.submitList(data)
        index?.let { memberAdapter.notifyItemChanged(it) }
        progressLoad.isVisible = false
    }

    override fun updateResult(results: List<Member>) {
        val stringBuilder = StringBuilder()
        results.forEach {
            stringBuilder.append(getString(R.string.mention_format, it.accountId, it.name))
        }
        setFragmentResult(REQUEST_MENTION, bundleOf(RESULT_MENTION to stringBuilder.toString()))
    }

    companion object {
        const val ROLE_TRAINER = "trainer"
        const val ROLE_DEFAULT = "default"
        const val REQUEST_MENTION = "request_mention"
        const val RESULT_MENTION = "result_mention"
    }
}
