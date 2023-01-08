package com.anonymous.getapifromflowcoroutines.domain.Usecases

import com.anonymous.getapifromflowcoroutines.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetListAccountsUsecase @Inject constructor(val chatRepository: ChatRepository) {
    suspend operator fun invoke() = chatRepository.getListUsers()
}