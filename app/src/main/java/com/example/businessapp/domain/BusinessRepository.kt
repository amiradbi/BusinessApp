package com.example.businessapp.domain

import com.example.businessapp.domain.model.Business
import io.reactivex.Single

interface BusinessRepository {
    fun getBusinesses(location: String, limit: Int, category: String): Single<List<Business>>
}