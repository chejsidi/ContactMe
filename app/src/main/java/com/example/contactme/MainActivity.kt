package com.example.contactme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.contactme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding: acceso a las vistas sin findViewById
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Conectar el NavController con el NavHostFragment del layout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // La pantalla de login no muestra flecha de "volver"
        val appBarConfig = AppBarConfiguration(setOf(R.id.loginFragment))
        setupActionBarWithNavController(navController, appBarConfig)
    }

    // Botón atrás de la ActionBar navega hacia atrás
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}