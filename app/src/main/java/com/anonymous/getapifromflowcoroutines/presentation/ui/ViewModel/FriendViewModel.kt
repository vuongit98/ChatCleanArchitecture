package com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anonymous.getapifromflowcoroutines.Utils.Resource
import com.anonymous.getapifromflowcoroutines.data.Model.Account
import com.anonymous.getapifromflowcoroutines.data.Model.Friend
import com.anonymous.getapifromflowcoroutines.domain.Usecases.usecases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FriendViewModel @Inject constructor(val usercase : usecases) : ViewModel() {
    private var friendFlow = MutableStateFlow(FriendState())
    var _friendFlow : StateFlow<FriendState> = friendFlow

    private var acceptFriend = MutableStateFlow(RegisterState(isLoading = true))
    var _acceptFriend : StateFlow<RegisterState> = acceptFriend


    fun getListUsers(uid : String) {
        viewModelScope.launch {
            val result = usercase.getListNotAddFriend(uid)
            result.collect{
                when(it){
                    is Resource.Loading ->{
                        friendFlow.value = FriendState(isLoading = true)
                    }
                    is Resource.Success ->{
                        friendFlow.value = FriendState(isLoading = false, data = it.data as MutableList<Friend>)
                    }
                    is Resource.Error ->{
                        friendFlow.value = FriendState(error = "An Error is unexpected")
                    }
                }
            }
        }
    }
    fun addFriends(account: Account, accountFriend: Account) {
        viewModelScope.launch {
            val result = usercase.addFriendUsecase(account = account, countFriend = accountFriend)
            result.collect {
                when(it){
                    is Resource.Loading ->{
                        friendFlow.value = FriendState(isLoading = true)
                    }
                    is Resource.Success ->{
                        friendFlow.value = FriendState(isLoading = false, data = it.data as MutableList<Friend>)
                    }
                    is Resource.Error ->{
                        friendFlow.value = FriendState(error = it.message.toString())
                    }
                }
            }
        }
    }
    fun acceptFriends(account: Account, accountFriend: Account) {
        viewModelScope.launch {
            val result = usercase.acceptFriendUsecase(account, accountFriend)
            result.collect {
                when(it){

                    is Resource.Loading ->{
                        acceptFriend.value = RegisterState(isLoading = true)
                    }
                    is Resource.Success -> {
                        acceptFriend.value = RegisterState(data = it.data!!)
                    }
                    is Resource.Error -> {
                        acceptFriend.value = RegisterState(error = "An error is unexpected ")
                    }
                }
            }
        }
    }
}