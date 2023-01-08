package com.anonymous.getapifromflowcoroutines.di

import com.anonymous.getapifromflowcoroutines.Utils.Constants.FIREBASE_URL
import com.anonymous.getapifromflowcoroutines.data.Repository.ChatRepositoryImpl
import com.anonymous.getapifromflowcoroutines.domain.Usecases.*
import com.anonymous.getapifromflowcoroutines.domain.repository.ChatRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase() : FirebaseDatabase {
        return FirebaseDatabase.getInstance(FIREBASE_URL);
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        database: FirebaseDatabase
    ):ChatRepository{
        return ChatRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideUsecases(chatRepository: ChatRepository) : usecases {
        return usecases(GetListAccountsUsecase(chatRepository),
                GetListFriendsUsecase(chatRepository),
                LoginUsecase(chatRepository),
                RegisterUsecase(chatRepository),
                GetListNotAddFriend(chatRepository),
                AddFriendUsecase(chatRepository),
                AcceptFriendUsecase(chatRepository)
            )
    }
}