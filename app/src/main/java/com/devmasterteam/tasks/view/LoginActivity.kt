package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.viewmodel.LoginViewModel

/**
 * 1) Chama a viewmodel -> LoginViewModel
 *
 */

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        // Observadores
        observe()
    }

    override fun onClick(v: View) {
        // faz a verificação de qual elemento da tela de login foi clicado
        if (v.id == R.id.button_login) {
            handleLogin() // método que trabalha com o botão de login
        }
    }

    // observa a val login da LoginViewModel, para saber a resposta do login (sucesso ou erro)
    private fun observe() {
        viewModel.login.observe(this) {
            // se der sucesso, se o status de login for positivo, faz a navegação/login
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish() // e encerra a Activity de login, permitindo que o usuário fique logado sem a necessidade de fazer login novamente
                // senão, mostra a mensagem de erro
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    // botão de login
    private fun handleLogin() {
        val email = binding.editEmail.text.toString() // pega o e-mail
        val password = binding.editPassword.text.toString() // pega a senha

        viewModel.doLogin(email, password) // chama a viewmodel (LoginViewModel) q tem a fun doLogin
    }
}