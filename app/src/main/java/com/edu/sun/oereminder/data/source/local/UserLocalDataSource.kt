package com.edu.sun.oereminder.data.source.local

import com.edu.sun.oereminder.data.model.Account
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.data.model.Room
import com.edu.sun.oereminder.data.source.UserDataSource

class UserLocalDataSource(private var dbHelper: DatabaseHelper) : UserDataSource.Local {

    override fun getProfile() = dbHelper.get(Account::class).firstOrNull()

    override fun getRooms() = dbHelper.get(Room::class)

    override fun getMembers() = dbHelper.get(Member::class)

    override fun updateProfile(account: Account) = dbHelper.replace(account)

    override fun updateRooms(rooms: List<Room>) = dbHelper.replace(rooms)

    override fun updateMember(members: List<Member>) = dbHelper.replace(members)

    companion object {
        @Volatile
        private var INSTANCE: UserLocalDataSource? = null

        fun getInstance(dbHelper: DatabaseHelper) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserLocalDataSource(dbHelper)
            }
    }
}
