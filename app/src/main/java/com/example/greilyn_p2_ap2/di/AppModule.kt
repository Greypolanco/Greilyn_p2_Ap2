package com.example.greilyn_p2_ap2.di

import com.example.greilyn_p2_ap2.data.GastosApi
import com.example.greilyn_p2_ap2.data.repository.GastosRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesGastosApi(moshi: Moshi): GastosApi {
        return Retrofit.Builder()
            .baseUrl("https://sag-api.azurewebsites.net")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GastosApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGastosRepository(gastosApi: GastosApi): GastosRepository {
        return GastosRepository(gastosApi)
    }
}