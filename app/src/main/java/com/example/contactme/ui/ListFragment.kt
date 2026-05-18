package com.example.contactme.ui

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.contactme.R
import com.example.contactme.databinding.FragmentListBinding
import com.example.contactme.viewmodel.ContactViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    // ViewModel compartido con otros fragments
    private val viewModel: ContactViewModel by viewModels()
    private lateinit var adapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupMenu()
        observeContacts()

        // FAB navega a añadir (sin contacto = modo creación)
        binding.fabAdd.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToEditFragment(contactId = -1)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter { contact ->
            // Al pulsar un contacto vamos a detalles
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(contact.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun observeContacts() {
        // Observa la BBDD y actualiza la lista automáticamente
        viewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            adapter.submitList(contacts)
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.menu_list, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_add -> {
                        val action = ListFragmentDirections.actionListFragmentToEditFragment(contactId = -1)
                        findNavController().navigate(action)
                        true
                    }
                    R.id.action_search -> {
                        showSearchDialog()
                        true
                    }
                    R.id.action_logout -> {
                        findNavController().navigate(R.id.action_listFragment_to_loginFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showSearchDialog() {
        val searchView = SearchView(requireContext())
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    observeContacts()
                } else {
                    viewModel.search(query).observe(viewLifecycleOwner) { contacts ->
                        adapter.submitList(contacts)
                    }
                }
                return true
            }
        })

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Buscar contacto")
            .setView(searchView)
            .setNegativeButton("Cerrar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}