package com.example.businessapp.application.injection.modules

import com.example.businessapp.BuildConfig
import com.example.businessapp.application.injection.interceptor.TokenInterceptor
import com.example.businessapp.data.BusinessSearchService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val BASE_URL = "https://api.yelp.com"

        fun buildRetrofit(httpClient: OkHttpClient): Retrofit =
                Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // return RX compatible responses
                        .addConverterFactory(MoshiConverterFactory.create())
                        .client(httpClient)
                        .build()
    }

    // region Http Clients
    @Provides
    @Singleton
    fun provideOkHttp(logger: HttpLoggingInterceptor,
                      tokenInterceptor: TokenInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(tokenInterceptor)
                    .addNetworkInterceptor(logger)
                    .build()


    @Provides
    @Singleton
    fun provideTokenInterceptor(): TokenInterceptor {
        //TODO:move token to more secure place later on
        val token = "sVVg7Cw5ktcRrJxpXgUkHdGzlWREMSW1rgUhA-hKWlQgjmwJqyS5S6av_RDZybt5MMpVObRuyGNOMDghOfHd3DR9yrtLWUmV71E8oA_ENgMZXUw2iTpXdHXZR4K3XXYx"
        return TokenInterceptor(token)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
    // endregion

    @Provides
    @Singleton
    fun provideBusinessSearchService(client: OkHttpClient): BusinessSearchService =
            buildRetrofit(client)
                    .create(BusinessSearchService::class.java)
}