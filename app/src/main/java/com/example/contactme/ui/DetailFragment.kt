package com.example.contactme.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contactme.R
import com.example.contactme.data.Contact
import com.example.contactme.databinding.FragmentDetailBinding
import com.example.contactme.viewmodel.ContactViewModel
import com.google.android.material.snackbar.Snackbar

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactViewModel by viewModels()

    // Safe Args: recibe el id del contacto desde ListFragment
    private val args: DetailFragmentArgs by navArgs()
    private var currentContact: Contact? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadContact()
        setupMenu()

        binding.btnEdit.setOnClickListener {
            navigateToEdit()
        }

        binding.btnDelete.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun loadContact() {
        // Observa todos los contactos y filtra por el id recibido
        viewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            val contact = contacts.find { it.id == args.contactId }
            contact?.let {
                currentContact = it
                bindContact(it)
            }
        }
    }

    private fun bindContact(contact: Contact) {
        binding.tvInitial.text = contact.name.first().uppercase()
        binding.tvName.text = contact.name
        binding.tvCategory.text = contact.category
        binding.tvEmail.text = contact.email
        binding.tvPhone.text = contact.phone
        binding.tvDetail.text = contact.detail
    }

    private fun navigateToEdit() {
        val action = DetailFragmentDirections.actionDetailFragmentToEditFragment(args.contactId)
        findNavController().navigate(action)
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("¿Eliminar?")
            .setMessage("Esta acción no se puede deshacer.")
            .setPositiveButton("Borrar") { _, _ ->
                currentContact?.let {
                    viewModel.delete(it)
                    com.google.android.material.snackbar.Snackbar
                        .make(binding.root, "Contacto eliminado", Snackbar.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.menu_detail, menu)
            }
            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_edit -> { navigateToEdit(); true }
                    R.id.action_delete -> { showDeleteDialog(); true }
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