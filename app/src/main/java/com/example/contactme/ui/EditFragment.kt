package com.example.contactme.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contactme.R
import com.example.contactme.data.Contact
import com.example.contactme.databinding.FragmentEditBinding
import com.example.contactme.viewmodel.ContactViewModel

class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactViewModel by viewModels()
    private val args: EditFragmentArgs by navArgs()

    private var existingContact: Contact? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Si contactId != -1 estamos editando, cargamos los datos
        if (args.contactId != -1) {
            loadContactForEdit()
        }

        binding.btnSave.setOnClickListener { saveContact() }

        setupMenu()
    }

    private fun loadContactForEdit() {
        viewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            val contact = contacts.find { it.id == args.contactId }
            contact?.let {
                existingContact = it
                binding.etName.setText(it.name)
                binding.etEmail.setText(it.email)
                binding.etPhone.setText(it.phone)
                binding.etDetail.setText(it.detail)
            }
        }
    }

    private fun saveContact() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val detail = binding.etDetail.text.toString().trim()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Nombre y email son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (existingContact != null) {
            // Modo edición: actualizamos el contacto existente conservando su id
            val updated = existingContact!!.copy(
                name = name, email = email, phone = phone, detail = detail
            )
            viewModel.update(updated)
            Toast.makeText(requireContext(), "Contacto actualizado", Toast.LENGTH_SHORT).show()
        } else {
            // Modo creación: insertamos contacto nuevo
            val newContact = Contact(
                name = name, email = email, phone = phone, detail = detail
            )
            viewModel.insert(newContact)
            Toast.makeText(requireContext(), "Contacto guardado", Toast.LENGTH_SHORT).show()
        }

        findNavController().popBackStack()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.menu_edit, menu)
            }
            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_save -> { saveContact(); true }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}