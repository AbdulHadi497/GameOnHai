package com.project.gameonhai.core.model

data class Court(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val imageUrls: List<String> = emptyList(),
    val description: String = "",
    val minPrice: Double = 0.0,
    val isAvailable: Boolean = true,
    val isSuspended: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val adminId: String = "",
    val games: List<String> = emptyList() // List of game IDs
)