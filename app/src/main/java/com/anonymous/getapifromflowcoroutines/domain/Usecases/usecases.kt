package com.anonymous.getapifromflowcoroutines.domain.Usecases

data class usecases(
    val getListAccountsUsecase: GetListAccountsUsecase,
    val getListFriendsUsecase: GetListFriendsUsecase,
    val loginUsecase: LoginUsecase,
    val registerUsecase: RegisterUsecase,
    val getListNotAddFriend: GetListNotAddFriend,
    val addFriendUsecase: AddFriendUsecase,
    val acceptFriendUsecase: AcceptFriendUsecase
)
