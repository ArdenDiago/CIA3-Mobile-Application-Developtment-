package com.example.cia3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * UserProfile Entity storing user information in the Room database.
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val name: String = "User",
    val email: String = "",
    val joinDate: String = "",
    val profilePictureUri: String? = null
)
