package com.example.businessapp.domain.model

data class Business(val name: String,
                    val address: Address,
                    val reviewCount: Int,
                    val rating: Double,
                    val imageUrl: String?) {

    data class Address(val line: List<String>,
                       val latitude: Double,
                       val longitude: Double)
}