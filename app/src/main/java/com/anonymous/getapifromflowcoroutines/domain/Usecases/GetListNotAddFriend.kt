package com.anonymous.getapifromflowcoroutines.domain.Usecases

import com.anonymous.getapifromflowcoroutines.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListNotAddFriend @Inject constructor(val chatRepository: ChatRepository) {
    suspend operator fun invoke(uid : String) = chatRepository.getListNotAddFriends(uid)
}