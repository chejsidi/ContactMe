package com.example.contactme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.contactme.R
import com.example.contactme.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Usuario y contraseña hardcodeados (sin BBDD de usuarios)
    private val USER = "maria"
    private val PASS = "1234"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val user = binding.etUsuario.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (user == USER && pass == PASS) {
                // Navegar al listado y borrar login de la pila
                findNavController().navigate(R.id.action_loginFragment_to_listFragment)
            } else {
                Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Limpiar binding al destruir la vista
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}