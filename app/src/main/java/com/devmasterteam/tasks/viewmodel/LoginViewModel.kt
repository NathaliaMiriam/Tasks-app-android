package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

/**
 * Para as regras de negócio, validações ...
 *
 * 2) Chama o repositório -> PersonRepository
 *
 * Chama a API
 *
 * Se conecta com a LoginActivity
 *
 * Se conecta com os repositórios: PersonRepository, PriorityRepository
 *
 * Se conecta com a SharedPreferences
 *
 * MutableLiveData -> muda de informação
 * LiveData -> não muda de informação
 *
 * download de prioridades -> obtenção da API -> retorno dos dados
 *
 */

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // instancia o repositório, visto que, lá foi inserido um contexto, e aqui eu consigo acessar o que tem lá
    private val personRepository = PersonRepository(application.applicationContext)

    // instancia o repositório de prioridades, visto que, lá foi inserido um contexto, e aqui eu consigo acessar o que tem lá
    private val priorityRepository = PriorityRepository(application.applicationContext)

    // instancia a SharedPreferences, visto que, lá foi inserido um contexto, e aqui eu consigo acessar o que tem lá
    private val securityPreferences = SecurityPreferences(application.applicationContext)


    // atribui p a Activity -> LoginActivity  o retorno obtido (sucesso ou falha) ref. ao login do usuário
    private val _login = MutableLiveData<ValidationModel>() // 'ValidationModel' é a classe que agrupa e atribui os status de login
    val login: LiveData<ValidationModel> = _login

    // atribui p a Activity -> LoginActivity  o retorno obtido (sucesso ou falha) ref. ao usuário estar ou não logado
    private val _loggedUser = MutableLiveData<Boolean>() // 'Boolean' é o retorno
    val loggedUser: LiveData<Boolean> = _loggedUser


     // para o login usando a API
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> { // passa as infos de login do usuário p o repositório

            // sucesso
            override fun onSuccess(result: PersonModel) { // 'result: PersonModel' -> tenho os dados de login do usuário

                // salva na SecurityPreferences os dados de login do usuário, após a captura -- 'store()' é a fun da SecurityPreferences que salva dados
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                // add os headers no RetrofitClient
                RetrofitClient.addHeaders(result.token, result.personKey)

                _login.value = ValidationModel() // instancia a ValidationModel vazia
            }

            // falha
            override fun onFailure(message: String) {
                _login.value = ValidationModel(message) // instancia a ValidationModel c a mensagem de erro respondendo ao usuário q o login deu errado
            }

        })
    }

    // verifica se o usuário está logado e transfere p a LoginActivity - 'get' é a fun da SecurityPreferences que pega dados
    fun verifyLoggedUser() {
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        // atualiza o header para que ele apareça em todas as requisições
        RetrofitClient.addHeaders(token, person)

        // se for avaliado como 'true' significa que o usuário está logado e é atribuído ao _loggedUser
        val logged = (token != "" && person != "")
        _loggedUser.value = logged

        // se for avaliado como 'false' significa que o usuário não está logado e é feito o download da lista de prioridades
        if (!logged) {
            priorityRepository.list(object : APIListener<List<PriorityModel>>{

                // sucesso
                override fun onSuccess(result: List<PriorityModel>) {
                    priorityRepository.save(result) // chama a fun que salva a lista de prioridades
                }

                // falha
                override fun onFailure(message: String) {
                    val f = ""
                }
            })
        }
    }

}