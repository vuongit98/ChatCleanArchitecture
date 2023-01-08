package com.anonymous.getapifromflowcoroutines.domain.Usecases

import com.anonymous.getapifromflowcoroutines.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUsecase @Inject constructor(val chatRepository: ChatRepository) {
    suspend operator fun invoke(email : String , pass : String) = chatRepository.login(email, pass)
}