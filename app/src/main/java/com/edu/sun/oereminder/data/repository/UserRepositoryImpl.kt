package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.AppExecutors
import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Account
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.data.model.Room
import com.edu.sun.oereminder.data.source.UserDataSource

class UserRepositoryImpl private constructor(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote,
    private val appExecutors: AppExecutors
) : UserRepository {

    override fun getProfile(callback: SourceCallback<Account?>) {
        with(appExecutors) {
            networkIO.execute {
                remote.getProfile(object : SourceCallback<Account> {

                    override fun onSuccess(data: Account) {
                        mainThread.execute { callback.onSuccess(data) }
                        diskIO.execute { local.updateProfile(data) }
                    }

                    override fun onError(e: Exception) {
                        diskIO.execute {
                            val cached = local.getProfile()
                            mainThread.execute {
                                callback.onError(e)
                                callback.onSuccess(cached)
                            }
                        }
                    }
                })
            }
        }
    }

    override fun getRooms(callback: SourceCallback<List<Room>>) {
        with(appExecutors) {
            networkIO.execute {
                remote.getRooms(object : SourceCallback<List<Room>> {

                    override fun onSuccess(data: List<Room>) {
                        diskIO.execute {
                            local.updateRooms(data)
                            mainThread.execute { callback.onSuccess(data) }
                        }
                    }

                    override fun onError(e: Exception) {
                        diskIO.execute {
                            val cached = local.getRooms()
                            mainThread.execute {
                                callback.onError(e)
                                callback.onSuccess(cached)
                            }
                        }
                    }
                })
            }
        }
    }

    override fun getMembers(callback: SourceCallback<List<Member>>) {
        with(appExecutors) {
            networkIO.execute {
                remote.getMembers(object : SourceCallback<List<Member>> {

                    override fun onSuccess(data: List<Member>) {
                        diskIO.execute {
                            local.updateMember(data)
                            mainThread.execute { callback.onSuccess(data) }
                        }
                    }

                    override fun onError(e: Exception) {
                        diskIO.execute {
                            val cached = local.getMembers()
                            mainThread.execute {
                                callback.onError(e)
                                callback.onSuccess(cached)
                            }
                        }
                    }
                })
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            local: UserDataSource.Local,
            remote: UserDataSource.Remote,
            appExecutors: AppExecutors
        ) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserRepositoryImpl(local, remote, appExecutors).also { INSTANCE = it }
        }
    }
}
