package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Account
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.data.model.Room

interface UserRepository {

    fun getProfile(callback: SourceCallback<Account?>)

    fun getRooms(callback: SourceCallback<List<Room>>)

    fun getMembers(callback: SourceCallback<List<Member>>)
}
