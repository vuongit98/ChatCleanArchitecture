package com.anonymous.getapifromflowcoroutines.presentation.ui.ViewModel

import com.anonymous.getapifromflowcoroutines.data.Model.Friend

data class FriendState(
    val isLoading : Boolean = false ,
    val data: MutableList<Friend> = mutableListOf() ,
    val error : String=""
)
