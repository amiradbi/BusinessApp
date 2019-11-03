package com.example.businessapp.data

import com.example.businessapp.data.model.SearchBusinessesApiResponse
import com.example.businessapp.data.model.SearchBusinessesApiResponse.Coordinates
import com.example.businessapp.data.model.SearchBusinessesApiResponse.Location
import com.example.businessapp.domain.model.Business
import com.example.businessapp.domain.model.Business.Address

object BusinessMapper {
    fun mapToDomain(response: SearchBusinessesApiResponse): List<Business> =
            response.businesses.map {
                Business(
                        name = it.name,
                        address = mapToAddress(it.location, it.coordinates),
                        reviewCount = it.review_count ?: 0,
                        rating = it.rating,
                        imageUrl = it.image_url
                )
            }

    private fun mapToAddress(location: Location, coordinates: Coordinates): Address =
            Address(line = location.display_address,
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude
            )
}