package com.example.businessapp.data.model

import com.squareup.moshi.Json

data class SearchBusinessesApiResponse(
        @Json(name = "businesses") val businesses: List<BusinessApiResponse>) {

    data class BusinessApiResponse(
            @Json(name = "name") val name: String,
            @Json(name = "image_url") val image_url: String?,
            @Json(name = "review_count") val review_count: Int? = 0,
            @Json(name = "rating") val rating: Double,
            @Json(name = "coordinates") val coordinates: Coordinates,
            @Json(name = "location") val location: Location
    )

    data class Coordinates(
            @Json(name = "latitude") val latitude: Double,
            @Json(name = "longitude") val longitude: Double
    )

    data class Location(
            @Json(name = "display_address") val display_address: List<String>
    )
}