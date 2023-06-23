package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.PersonRepository

/**
 * 2) Chama o repositório -> PersonRepository
 *
 *
 */

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository()


     // Faz login usando API
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> { // passa as infos de login do usuário p o repositório

            // responde ao usuário se o login deu certo
            override fun onSuccess(result: PersonModel) {

            }

            // responde ao usuário se o login deu errado
            override fun onFailure(message: String) {

            }

        })
    }


    // Verifica se usuário está logado
    fun verifyLoggedUser() {
    }

}