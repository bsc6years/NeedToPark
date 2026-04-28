package com.example.parkingfinder.di

import com.example.parkingfinder.data.BookingRepository
import com.example.parkingfinder.data.ParkingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideParkingRepository(db: FirebaseFirestore): ParkingRepository =
        ParkingRepository(db)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideBookingRepository(db: FirebaseFirestore): BookingRepository =
        BookingRepository(db)
}
