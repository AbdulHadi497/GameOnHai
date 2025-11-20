package com.project.gameonhai.di


import com.google.firebase.firestore.FirebaseFirestore
import com.project.gameonhai.categories.data.CategoryRepositoryImpl
import com.project.gameonhai.core.data.repository.CategoryRepository
import com.project.gameonhai.core.data.repository.CourtRepository
import com.project.gameonhai.core.data.repository.GameRepository
import com.project.gameonhai.core.data.repository.TimeSlotRepository
import com.project.gameonhai.core.network.firebase.FirebaseBookingService
import com.project.gameonhai.core.network.firebase.FirebaseCategoryService
import com.project.gameonhai.core.network.firebase.FirebaseCourtService
import com.project.gameonhai.core.network.firebase.FirebaseGameService
import com.project.gameonhai.core.network.firebase.FirebaseTimeSlotService
import com.project.gameonhai.court.data.CourtRepositoryImpl
import com.project.gameonhai.court.data.GameRepositoryImpl
import com.project.gameonhai.court.data.TimeSlotRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ========== Firebase ==========

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    // ========== Firebase Services ==========

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
    fun provideFirebaseGameService(
        firestore: FirebaseFirestore
    ): FirebaseGameService {
        return FirebaseGameService(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseTimeSlotService(
        firestore: FirebaseFirestore
    ): FirebaseTimeSlotService {
        return FirebaseTimeSlotService(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseBookingService(
        firestore: FirebaseFirestore
    ): FirebaseBookingService {
        return FirebaseBookingService(firestore)
    }

    // ========== Repositories ==========

    @Provides
    @Singleton
    fun provideCourtRepository(
        courtService: FirebaseCourtService
    ): CourtRepository {
        return CourtRepositoryImpl(courtService)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryService: FirebaseCategoryService
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryService)
    }

    @Provides
    @Singleton
    fun provideGameRepository(
        gameService: FirebaseGameService
    ): GameRepository {
        return GameRepositoryImpl(gameService)
    }

    @Provides
    @Singleton
    fun provideTimeSlotRepository(
        timeSlotService: FirebaseTimeSlotService
    ): TimeSlotRepository {
        return TimeSlotRepositoryImpl(timeSlotService)
    }
}