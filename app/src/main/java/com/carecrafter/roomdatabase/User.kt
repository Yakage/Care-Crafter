package com.carecrafter.roomdatabase
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    val userId: String
)
