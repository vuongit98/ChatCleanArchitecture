package com.anonymous.getapifromflowcoroutines.domain.repository

import com.anonymous.getapifromflowcoroutines.Utils.Resource
import com.anonymous.getapifromflowcoroutines.data.Model.Account
import com.anonymous.getapifromflowcoroutines.data.Model.Friend
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getListUsers() :Flow<Resource<List<Account>>>
    suspend fun login(email : String, password : String) : Flow<Resource<Account>>
    suspend fun register(account : Account) : Flow<Resource<Boolean>>
    suspend fun getListFriends(uid : String) : Flow<Resource<List<Account>>>
    suspend fun getListNotAddFriends(uid: String) : Flow<Resource<MutableList<Friend>>>
    suspend fun addFriends(account: Account, accountFriend : Account) : Flow<Resource<MutableList<Friend>>>
    suspend fun acceptFriends(account: Account, accountFriend : Account) : Flow<Resource<Boolean>>
}