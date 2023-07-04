package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository

/**
 * 2) Chama o repositório -> PersonRepository
 *
 *
 * MutableLiveData -> muda de informação
 * LiveData -> não muda de informação
 *
 */

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext) // passa o contexto p o repositório, visto que, lá foi inserido um contexto

    // var observada pela LoginActivity na fun observe() - ValidationModel é a classe que agrupa e atribui os status de login
    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login


     // Faz login usando API
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> { // passa as infos de login do usuário p o repositório

            // responde ao usuário se o login deu certo
            override fun onSuccess(result: PersonModel) {
                _login.value = ValidationModel() // se for sucesso instancia a ValidationModel
            }

            // responde ao usuário se o login deu errado
            override fun onFailure(message: String) {
                _login.value = ValidationModel(message) // se não for sucesso instancia a ValidationModel c a mensagem de erro
            }

        })
    }

    // Verifica se usuário está logado
    fun verifyLoggedUser() {
    }

}