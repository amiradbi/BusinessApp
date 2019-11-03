package com.example.businessapp.data

import com.example.businessapp.domain.BusinessRepository
import com.example.businessapp.domain.model.Business
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessManager @Inject constructor(private val service: BusinessSearchService) : BusinessRepository {

    override fun getBusinesses(location: String, limit: Int, category: String): Single<List<Business>> =
            service.updateRates(location = location, limit = limit, categories = category)
                    .map(BusinessMapper::mapToDomain)
                    .onErrorResumeNext { Single.error(it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
}