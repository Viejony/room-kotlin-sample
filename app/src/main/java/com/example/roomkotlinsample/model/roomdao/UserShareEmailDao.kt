package com.example.roomkotlinsample.model.roomdao

import androidx.room.*

@Dao
interface UserShareEmailDao {
    @Query("SELECT * FROM UserShareEmail")
    fun getAll(): List<UserShareEmail>

    @Query("SELECT * FROM UserShareEmail WHERE id LIKE :id")
    fun findById(id: Long): UserShareEmail

    @Query("SELECT * FROM UserShareEmail WHERE email = :email")
    fun findByEmail(email: String): List<UserShareEmail>

    @Query("SELECT * FROM UserShareEmail WHERE owner = :owner")
    fun findByOwner(owner: String): List<UserShareEmail>

    @Query("SELECT * FROM UserShareEmail WHERE email = :email AND owner = :owner")
    fun findByEmailAndOwner(email: String, owner: String): UserShareEmail

    @Insert
    fun insert(email: UserShareEmail);

    @Delete
    fun delete(email: UserShareEmail)

    @Query("DELETE FROM UserShareEmail")
    fun deleteAll()

    @Update
    fun update(vararg email: UserShareEmail)
}