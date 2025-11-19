package com.project.gameonhai.di


import com.google.firebase.firestore.FirebaseFirestore
import com.project.gameonhai.categories.data.CategoryRepositoryImpl
import com.project.gameonhai.core.data.repository.CategoryRepository
import com.project.gameonhai.core.data.repository.CourtRepository
import com.project.gameonhai.core.network.firebase.FirebaseCategoryService
import com.project.gameonhai.core.network.firebase.FirebaseCourtService
import com.project.gameonhai.court.data.CourtRepositoryImpl
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
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseCourtService(
        firestore: FirebaseFirestore
    ): FirebaseCourtService {
        return FirebaseCourtService(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseCategoryService(
        firestore: FirebaseFirestore
    ): FirebaseCategoryService {
        return FirebaseCategoryService(firestore)
    }

    @Provides
    @Singleton
    fun provideCourtRepository(
        firebaseCourtService: FirebaseCourtService
    ): CourtRepository {
        return CourtRepositoryImpl(firebaseCourtService)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        firebaseCategoryService: FirebaseCategoryService
    ): CategoryRepository {
        return CategoryRepositoryImpl(firebaseCategoryService)
    }
}