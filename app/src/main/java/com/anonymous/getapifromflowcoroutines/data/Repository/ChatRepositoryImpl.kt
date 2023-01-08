package com.anonymous.getapifromflowcoroutines.data.Repository

import android.util.Log
import com.anonymous.getapifromflowcoroutines.Utils.Resource
import com.anonymous.getapifromflowcoroutines.data.Model.Account
import com.anonymous.getapifromflowcoroutines.data.Model.Friend
import com.anonymous.getapifromflowcoroutines.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(val firebaseDatabase: FirebaseDatabase) : ChatRepository {
    override suspend fun getListUsers(): Flow<Resource<List<Account>>> = flow {
        try {
            emit(Resource.Loading<List<Account>>())
            val results = firebaseDatabase.getReference("Accounts").get().await().children.map {
                    dataSnapshot -> dataSnapshot.getValue(Account::class.java)
            }
            emit(Resource.Success<List<Account>>(data = results as List<Account>))
        }catch (e : Exception) {
            emit(Resource.Error<List<Account>>(null, message = e.message.toString()))
        }

    }

    override suspend fun login(email: String, password: String): Flow<Resource<Account>> = flow{
        try {
            emit(Resource.Loading<Account>())
            val uid = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
            val result = firebaseDatabase.getReference("Accounts/${uid.user!!.uid}").get().await()
            result?.let {

                val account = result.getValue(Account::class.java)
                Log.d("login: ","${account}")
                if (account == null ) {
                    emit(Resource.Error<Account>(null,"Account not exist"))
                }else {
                    emit(Resource.Success<Account>(account))
                }
            }

        }catch (e : Exception) {
            emit(Resource.Error<Account>(null,e.message.toString()))
        }

    }

    override suspend fun register( account: Account): Flow<Resource<Boolean>> = flow  {
        try {
            emit(Resource.Loading<Boolean>())
            account?.let {
                Log.d( "register: ","VAO")
                if (it != null) {
                    val result = FirebaseAuth.getInstance().createUserWithEmailAndPassword(it.email, it.password)
                        .await()
                    result?.let {
                        account.uid = it.user!!.uid
                    }
                    firebaseDatabase.getReference("Accounts/${account.uid}").setValue(account)
                        .addOnCompleteListener {  }
                        .addOnFailureListener {
                            Resource.Error<Boolean>(false, it.message.toString())
                        }
                        .await()
                    emit(Resource.Success<Boolean>(true))
                }else {
                    emit(Resource.Error<Boolean>(null,"Not null data"))
                }
            }

        }catch (e : Exception) {
            emit(Resource.Error(null, e.message.toString()))
        }

    }

    override suspend fun getListFriends(uid : String ): Flow<Resource<List<Account>>> = flow {
        emit(Resource.Loading<List<Account>>())
        val results = firebaseDatabase.getReference("Friends/${uid}").get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Account::class.java)
        }
        emit(Resource.Success<List<Account>>(results as List<Account>))
    }

    override suspend fun getListNotAddFriends(uid: String): Flow<Resource<MutableList<Friend>>> = flow {

        try {
            emit(Resource.Loading<MutableList<Friend>>())
            val resultAccount = firebaseDatabase.getReference("Accounts").get().await().children.map {
                    dataSnapshot -> dataSnapshot.getValue(Account::class.java)
            }.filter {
                it?.uid != uid
            }

            Log.d( "getListNotAddFriends1: ", "${resultAccount}")
            val friendAccount = firebaseDatabase.getReference("Friends/${uid}").get().await()
            var resultFriend: List<Friend?> = mutableListOf()
            Log.d( "ListNotAddFriends-1: ", "${friendAccount.exists()}")
            if (friendAccount.exists() && friendAccount.hasChildren()){

                resultFriend = friendAccount.children.map { dataSnapshot -> dataSnapshot.getValue(Friend::class.java) }
            }
           // Log.d( "getListNotAddFriends2: ", "${resultFriend}")

            var listResult = mutableListOf<Friend>()
            if (resultFriend.size > 0) {

                for(data in resultAccount) {
                    var status = 0 ;
                    for (data1 in resultFriend) {
                        if (data?.uid == data1?.account?.uid) {
                            status = data1!!.status
                            break
                        }
                    }
                    data?.let {
                        val friend = Friend(it, status)
                        listResult.add(friend)
                    }
                }
            }else {
                for (data in resultAccount){
                    data ?.let {
                        listResult.add( Friend(it, 0))
                    }
                }
            }
            Log.d( "getListNotAddFriends3: ", "${listResult}")

            emit(Resource.Success<MutableList<Friend>>(listResult))
        }catch (e : Exception){
            emit(Resource.Error<MutableList<Friend>>(null, e.message.toString()))
        }


    }

    override suspend fun addFriends(account: Account, accountFriend : Account): Flow<Resource<MutableList<Friend>>> = flow {
        try {
            emit(Resource.Loading<MutableList<Friend>>())
            val resultAccount = firebaseDatabase.getReference("Accounts").get().await().children.map {
                    dataSnapshot -> dataSnapshot.getValue(Account::class.java)
            }.filter {
                it?.uid != account.uid
            }
            val accountRequest = Friend(account, 3)
            val accountReceiver = Friend(accountFriend, 1)

            firebaseDatabase.getReference("Friends/${account.uid}/${accountFriend.uid}").setValue(accountReceiver).await()
            firebaseDatabase.getReference("Friends/${accountFriend.uid}/${account.uid}").setValue(accountRequest).await()
            Log.d( "getListNotAddFriends: ", "${resultAccount}")
            val friendAccount = firebaseDatabase.getReference("Friends/${account.uid}").get().await()
            var resultFriend: List<Friend?> = mutableListOf()
            if (friendAccount.exists() && friendAccount.hasChildren()){
                resultFriend = friendAccount.children.map { dataSnapshot -> dataSnapshot.getValue(Friend::class.java) }
            }
            Log.d( "getListNotAddFriends: ", "${resultFriend}")

            var listResult = mutableListOf<Friend>()
            if (resultFriend.size > 0) {

                for(data in resultAccount) {
                    var status = 0 ;
                    for (data1 in resultFriend) {
                        if (data?.uid == data1?.account?.uid) {
                            status = data1!!.status
                            break
                        }
                    }
                    data?.let {
                        val friend = Friend(it, status)
                        listResult.add(friend)
                    }
                }
            }else {
                for (data in resultAccount){
                    data ?.let {
                        listResult.add(Friend(it,0))
                    }
                }
            }
            Log.d( "getListNotAddFriends: ", "${listResult}")

            emit(Resource.Success<MutableList<Friend>>(listResult))
        }catch (e : Exception){
            emit(Resource.Error<MutableList<Friend>>(null, e.message.toString()))
        }
    }

    override suspend fun acceptFriends(account: Account, accountFriend : Account): Flow<Resource<Boolean>> = flow {
        Resource.Loading<Boolean>()
        try {
            firebaseDatabase.getReference("Friends/${account.uid}/${accountFriend.uid}/status").setValue(2).await()
            firebaseDatabase.getReference("Friends/${accountFriend.uid}/${account.uid}/status").setValue(2).await()
            Resource.Success<Boolean>(true)
        }catch (e : Exception) {
            Resource.Error<Boolean>(null, e.message.toString())
        }
    }

}