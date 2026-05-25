package com.example.contactme.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactme.data.Contact
import com.example.contactme.databinding.ItemContactBinding

// ListAdapter actualiza solo las filas que cambian (eficiente)
class ContactAdapter(
    private val onClick: (Contact) -> Unit
) : ListAdapter<Contact, ContactAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ItemContactBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            // Inicial del nombre en el círculo
            binding.tvInitial.text = contact.name.first().uppercase()
            binding.tvName.text = contact.name
            binding.tvEmail.text = contact.email

            binding.root.setOnClickListener { onClick(contact) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Compara items para saber qué ha cambiado
    companion object DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(a: Contact, b: Contact) = a.id == b.id
        override fun areContentsTheSame(a: Contact, b: Contact) = a == b
    }
}