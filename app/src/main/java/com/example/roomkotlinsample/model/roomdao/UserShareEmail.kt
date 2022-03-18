package com.example.roomkotlinsample.model.roomdao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserShareEmail(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var email: String = "",
    var owner: String = ""
)