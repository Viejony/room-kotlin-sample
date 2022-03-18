package com.example.roomkotlinsample.model.roomdao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserShareEmail::class], version = 1)
abstract class UserShareEmailDatabase : RoomDatabase() {
    abstract fun userEmailsDao(): UserShareEmailDao
}
