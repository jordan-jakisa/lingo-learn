package com.kerustudios.lingolearn.di

import com.kerustudios.lingolearn.data.repositories.AuthRepository
import com.kerustudios.lingolearn.domain.usecases.AuthImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authImpl: AuthImpl): AuthRepository


}