package com.edu.sun.oereminder.data.source

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Account
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.data.model.Room

interface UserDataSource {

    interface Local {
        fun getProfile(): Account?

        fun getRooms(): List<Room>

        fun getMembers(): List<Member>

        fun updateProfile(account: Account)

        fun updateRooms(rooms: List<Room>)

        fun updateMember(members: List<Member>)
    }

    interface Remote {
        fun getProfile(callback: SourceCallback<Account>)

        fun getRooms(callback: SourceCallback<List<Room>>)

        fun getMembers(callback: SourceCallback<List<Member>>)
    }
}
