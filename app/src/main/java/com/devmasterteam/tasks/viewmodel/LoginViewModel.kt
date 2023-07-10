package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

/**
 * Para as regras de negócio, validações ...
 *
 * 2) Chama o repositório -> PersonRepository
 *
 *
 * MutableLiveData -> muda de informação
 * LiveData -> não muda de informação
 *
 */

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // passa o contexto p o repositório, visto que, lá foi inserido um contexto, e aqui eu consigo acessar o que tem lá
    private val personRepository = PersonRepository(application.applicationContext)

    // passa o contexto p a SharedPreferences, visto que, lá foi inserido um contexto, e aqui eu consigo acessar o que tem lá
    private val securityPreferences = SecurityPreferences(application.applicationContext)


    // var observada pela LoginActivity na fun observe() - ValidationModel é a classe que agrupa e atribui os status de login
    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    // var observada pela LoginActivity na fun observe()
    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser



     // Faz login usando API
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> { // passa as infos de login do usuário p o repositório

            // responde ao usuário se o login deu certo
            override fun onSuccess(result: PersonModel) { // no 'result: PersonModel' eu tenho os dados de login do usuário

                // salva na SecurityPreferences os dados de login do usuário, após a captura -- 'store()' é a fun da SecurityPreferences que salva dados
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                // add os headers no RetrofitClient
                RetrofitClient.addHeaders(result.token, result.personKey)

                // se for sucesso instancia a ValidationModel
                _login.value = ValidationModel()
            }

            // responde ao usuário se o login deu errado
            override fun onFailure(message: String) {
                _login.value = ValidationModel(message) // se não for sucesso instancia a ValidationModel c a mensagem de erro
            }

        })
    }

    // Verifica se o usuário está logado e transfere p a LoginActivity- 'get' é a fun da SecurityPreferences que pega dados
    fun verifyLoggedUser() {
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        // atualiza o header para que ele apareça em todas as requisições
        RetrofitClient.addHeaders(token, person)

        // se for avaliado como 'true' significa que o usuário está logado e é atribuído ao _loggedUser
        _loggedUser.value = (token != "" && person != "")
    }

}