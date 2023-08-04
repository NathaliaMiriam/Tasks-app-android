package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.*
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityMainBinding
import com.devmasterteam.tasks.viewmodel.MainViewModel

/**
 * Faz a config. do menu
 */

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setSupportActionBar(binding.appBarMain.toolbar)

        // 'fab' -> floating action button - ao clicar no botão acontece a navegação de uma activity p a outra -> 'TaskFormActivity' é p onde quero ir
        binding.appBarMain.fab.setOnClickListener {
            startActivity(Intent(applicationContext, TaskFormActivity::class.java))
        }

        // navegação
        setupNavigation()

        // chama/recebe o nome do usuário
        viewModel.loadUserName()

        // observadores
        observe()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupNavigation() {
        val drawerLayout: DrawerLayout = binding.drawerLayout // 'DrawerLayout' é a gaveta do menu, é quando arrasta/abre o menu da esquerda p direita
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_all_tasks, R.id.nav_next_tasks, R.id.nav_expired), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // trata o clique do item 'sair' do menu
        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.nav_logout) { // se o clique for no botão 'sair'...
                viewModel.logout() // chama o 'logout' da MainViewModel, permitindo a saída do app ...
                startActivity(Intent(applicationContext, LoginActivity::class.java)) // volta para a Activity de login
                finish() // e mata a Activity principal
            } else { // caso contrário, se o clique ñ for no botão 'sair' ... termina a interrupção e as coisas voltam a acontecer como programado anteriormente
                NavigationUI.onNavDestinationSelected(it, navController) // 'it' é o MenuItem
                drawerLayout.closeDrawer(GravityCompat.START) // fecha a gaveta do menu passando a orientação de p onde ela tem q fechar, 'START' -> esquerda, fecha p esquerda
            }
            true
        }
    }

    private fun observe() {
        // observa a var da MainViewModel p saber o retorno ref. ao nome do usuário
        viewModel.name.observe(this) {
            // identifica, busca e atribui o nome ao menu
            val header = binding.navView.getHeaderView(0) // identifica o elemento de layout - 'getHeaderView' pega o header, o index é 0 pq tem somente 1 header -> activity_main.xml
            header.findViewById<TextView>(R.id.text_name).text = it // busca o elem. pelo tipo, q é um 'TextView' e pelo id - atribui
        }
    }
}