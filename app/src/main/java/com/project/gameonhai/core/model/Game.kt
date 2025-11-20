package com.project.gameonhai.core.model

data class Game(
    val id: String = "",
    val courtId: String = "",
    val name: String = "", // e.g., "Futsal", "Cricket", "Padel"
    val pricePerHour: Double = 0.0,
    val isAvailable: Boolean = true,
    val description: String = "",
    val categoryId: String = "" // Links to Category collection
)