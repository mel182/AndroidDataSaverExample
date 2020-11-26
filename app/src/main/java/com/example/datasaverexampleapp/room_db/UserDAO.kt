package com.example.datasaverexampleapp.room_db

import androidx.room.*
import com.example.datasaverexampleapp.type_alias.User

@Dao
interface UserDAO
{
    // OnConflictStrategy options:
    // * ABORT = Cancel the ongoing transaction
    // * FAIL = Cause the current transaction to fail
    // * IGNORE = Ignore the conflicting new data and continue the transaction
    // * REPLACE = Override the existing value with the newly supplied value and continue the transaction
    // * ROLLBACK = Roll back the current transaction, reversing any previously made changes
    @Insert
    fun insertUser(userList: List<UserEntity>)

    @Insert
    fun insertUser(userEntity: UserEntity)

    @Update
    fun updateUser(vararg userList: UserEntity)

    @Update
    fun updateUser(userList: UserEntity)

    @Delete
    fun deleteUser(userList: UserEntity)

    @Query("SELECT * FROM user")
    fun loadAllUsers():List<UserEntity>

    @Query("SELECT * FROM user WHERE name = :name")
    fun loadAllUserByName(name:String):UserEntity

    @Query("DELETE FROM user")
    fun deleteAllUsers()

    @Query("SELECT * FROM user WHERE name IN (:name)")
    fun loadAllUserByName(name: Array<String>):List<UserEntity>

    @Query("SELECT name,age FROM user WHERE id = :userID")
    fun getNameAgeByID(userID: Int): List<NameAgeUserEntity>

    @Query("SELECT age FROM user WHERE id = :userID")
    fun getAgeByID(userID: Int):Int

}