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

        // variáveis da classe
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // layout
        setContentView(binding.root)

        // eventos de clique
        binding.buttonLogin.setOnClickListener(this) // botão de login
        binding.textRegister.setOnClickListener(this) // botão de novo cadastro

        // chama a 'verifyLoggedUser()' p informar se os dados do usuário estão preenchidos, ou seja, se o usuário está logado ou não
        viewModel.verifyLoggedUser()

        // observadores
        observe()
    }

    // implementa o evento de clique nos botões da tela de login -> login e cadastro
    override fun onClick(v: View) {
        // se for o botão de login... chama o método que trata o botão de login
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {  // mas se for o botão de cadastro... abre/inicia a Activity responsável pelo cadastro
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


    private fun observe() {
        // observa a val login da LoginViewModel para saber a resposta do login (sucesso ou erro)
        viewModel.login.observe(this) {
            // se der sucesso, se o status de login for positivo, faz a navegação/login
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish() // e encerra a Activity de login, permitindo que o usuário fique logado sem a necessidade de fazer login novamente
                // caso contrário, mostra a mensagem de erro
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        // observa a val loggedUser da LoginViewModel para saber se o usuário está logado ou não
        viewModel.loggedUser.observe(this) {
            if (it) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish() // encerra a Activity
            }
        }
    }

    // trata o botão de login
    private fun handleLogin() {
        val email = binding.editEmail.text.toString() // pega o e-mail
        val password = binding.editPassword.text.toString() // pega a senha

        viewModel.doLogin(email, password) // chama a viewmodel (LoginViewModel) q tem a fun doLogin
    }
}