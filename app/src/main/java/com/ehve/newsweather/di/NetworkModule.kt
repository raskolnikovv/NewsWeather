package com.ehve.newsweather.di

import com.ehve.newsweather.BuildConfig
import com.ehve.newsweather.data.remote.NewsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module // Diz ao Hilt que este é um módulo de dependências
@InstallIn(SingletonComponent::class) // Define que as dependências aqui duram enquanto o app estiver aberto
object NetworkModule {

    private const val BASE_URL = "https://newsapi.org/"

    @Provides // Diz que esta função fornece uma dependência
    @Singleton // Garante que só exista UMA instância do Retrofit no app todo (Economia de memória)
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Converte o JSON automaticamente para Objetos Kotlin
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        // Aqui o Hilt automaticamente pega o Retrofit criado acima e cria o serviço
        return retrofit.create(NewsApiService::class.java)
    }
}