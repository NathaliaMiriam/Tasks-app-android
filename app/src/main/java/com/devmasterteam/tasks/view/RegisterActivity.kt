package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.viewmodel.LoginViewModel
import com.devmasterteam.tasks.viewmodel.RegisterViewModel

/**
 * Activity responsável pelo cadastro de usuário
 */

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // variáveis da classe
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        // evento de clique p o botão de salvar cadastro
        binding.buttonSave.setOnClickListener(this)

        observe()

        // layout
        setContentView(binding.root)
    }

    // implementa o evento de clique p o botão de salvar cadastro
    override fun onClick(v: View) {
        if (v.id == R.id.button_save) {
            handleSave() // chama o método que trata o botão de cadastro
        }
    }

    // observa as vars da RegisterViewModel p saber o que está acontecendo
    private fun observe() {
        viewModel.user.observe(this) {
            // se obtiver sucesso no cadastro ... o usuário é levado p a Activity principal
            if (it.status()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else { // caso contrário, se não obtiver sucesso no cadastro ... a mensagem de falha é mostrada ao usuário
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    // trata o botão de cadastro
    private fun handleSave() {
        // busca as infos que são colocadas pelo usuário p realizar o cadastro
        val name = binding.editName.text.toString() // nome
        val email = binding.editEmail.text.toString() // e-mail
        val password = binding.editPassword.text.toString() // senha

        viewModel.create(name, email, password) // passa as infos obtidas p a RegisterViewModel
    }
}