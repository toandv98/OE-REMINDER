package com.edu.sun.oereminder.ui.settings

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.data.repository.UserRepository
import com.edu.sun.oereminder.ui.settings.MentionDialog.Companion.ROLE_DEFAULT
import com.edu.sun.oereminder.ui.settings.MentionDialog.Companion.ROLE_TRAINER

class MentionPresenter(
    private val userRepository: UserRepository,
    private val view: MentionContract.View
) : MentionContract.Presenter {

    private val memberList = mutableListOf<Member>()

    override fun loadScreen() {
        userRepository.getMembers(object : SourceCallback<List<Member>> {
            override fun onSuccess(data: List<Member>) {
                memberList.addAll(data)
                view.updateRecyclerView(memberList)
            }

            override fun onError(e: Exception) {}
        })
    }

    override fun onItemClick(member: Member, index: Int) {
        member.run {
            role = if (role == ROLE_TRAINER) ROLE_DEFAULT else ROLE_TRAINER
            memberList[index] = member
            view.updateRecyclerView(memberList, index)
        }
    }

    override fun onSaveClick() {
        view.updateResult(memberList.filter { it.role == ROLE_TRAINER })
    }
}
