package com.example.contactme.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {

    // Devuelve todos los contactos, se actualiza automáticamente
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAll(): LiveData<List<Contact>>

    @Insert
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)
}