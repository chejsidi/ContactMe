package com.example.contactme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.contactme.data.Contact
import com.example.contactme.data.ContactDatabase
import com.example.contactme.repository.ContactRepository
import kotlinx.coroutines.launch

// Conecta la UI con el repositorio, sobrevive a rotaciones de pantalla
class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactRepository
    val allContacts: LiveData<List<Contact>>

    init {
        val dao = ContactDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(dao)
        allContacts = repository.allContacts
    }

    fun insert(contact: Contact) = viewModelScope.launch {
        repository.insert(contact)
    }

    fun update(contact: Contact) = viewModelScope.launch {
        repository.update(contact)
    }

    fun delete(contact: Contact) = viewModelScope.launch {
        repository.delete(contact)
    }

    fun search(query: String): LiveData<List<Contact>> = repository.search(query)
}