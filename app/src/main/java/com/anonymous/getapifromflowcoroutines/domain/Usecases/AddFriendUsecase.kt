package com.anonymous.getapifromflowcoroutines.domain.Usecases

import com.anonymous.getapifromflowcoroutines.data.Model.Account
import com.anonymous.getapifromflowcoroutines.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddFriendUsecase @Inject constructor(val chatRepository: ChatRepository) {
    suspend operator fun invoke(account : Account, countFriend: Account) =  chatRepository.addFriends(account = account, accountFriend = countFriend )
}