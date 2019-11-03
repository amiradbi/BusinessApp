package com.example.businessapp.data

import com.example.businessapp.data.model.SearchBusinessesApiResponse
import com.example.businessapp.domain.model.Business
import org.junit.Assert.assertEquals
import org.junit.Test

class BusinessMapperTest {

    private val apiResponse =
            SearchBusinessesApiResponse(businesses = listOf(
                    SearchBusinessesApiResponse.BusinessApiResponse(name = "Alexander",
                            image_url = "google.com",
                            review_count = 10,
                            rating = 4.0,
                            coordinates = SearchBusinessesApiResponse.Coordinates(43.231, 56.78),
                            location = SearchBusinessesApiResponse.Location(display_address = listOf("44 Burke st",
                                    "Melbourne")))
            ))

    @Test
    fun mapToDomain() {
        val expected = listOf(
                Business(name = "Alexander",
                        address = Business.Address(line = listOf("44 Burke st", "Melbourne"), latitude = 43.231, longitude = 56.78),
                        rating = 4.0,
                        reviewCount = 10,
                        imageUrl = "google.com")
        )

        assertEquals(expected, BusinessMapper.mapToDomain(apiResponse))
    }
}