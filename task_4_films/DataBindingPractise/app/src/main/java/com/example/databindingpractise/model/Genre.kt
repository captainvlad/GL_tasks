package com.example.databindingpractise.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Long = -1L,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String = "Sorry, error with server connection",
    @ColumnInfo(name = "expirationDate")
    val expirationDate: Long = System.currentTimeMillis()
)