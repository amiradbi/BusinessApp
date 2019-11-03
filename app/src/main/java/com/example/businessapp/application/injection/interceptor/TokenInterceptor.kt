package com.example.businessapp.application.injection.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * adds token to api calls
 */
class TokenInterceptor constructor(private val authToken: String) : Interceptor {
    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request() ?: throw IOException()

        return chain.proceed(request.newBuilder()
                .addHeader(AUTHORIZATION_HEADER, "$BEARER $authToken")
                .build())
    }
}