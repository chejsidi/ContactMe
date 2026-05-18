package com.example.contactme.repository

import androidx.lifecycle.LiveData
import com.example.contactme.data.Contact
import com.example.contactme.data.ContactDao

// Intermediario entre ViewModel y la BBDD
class ContactRepository(private val dao: ContactDao) {

    val allContacts: LiveData<List<Contact>> = dao.getAll()

    fun search(query: String): LiveData<List<Contact>> = dao.search(query)

    suspend fun insert(contact: Contact) = dao.insert(contact)

    suspend fun update(contact: Contact) = dao.update(contact)

    suspend fun delete(contact: Contact) = dao.delete(contact)
}