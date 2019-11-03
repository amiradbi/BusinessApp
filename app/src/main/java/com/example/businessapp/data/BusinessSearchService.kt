package com.example.businessapp.data


import com.example.businessapp.data.model.SearchBusinessesApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BusinessSearchService {
    @GET("/v3/businesses/search")
    fun updateRates(@Query("location") location: String,
                    @Query("limit") limit: Int,
                    @Query("categories") categories: String): Single<SearchBusinessesApiResponse>
}