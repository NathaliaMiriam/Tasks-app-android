package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositório que orquestra as chamadas para o banco de dados ou API, neste caso, API
 *
 * 3) Chama a PersonService (serviço de informações do usuário que usa o Retrofit)
 *
 * Se conecta com a viewmodel -> LoginViewModel
 *
 * Se conecta com o serviço (PersonService) para verificar se as infos do usuário estão ok
 *
 * Se conecta com a PersonModel (que mapeia as infos retornadas, as infos que chegam do usuário, quando o mesmo faz login ou criação de uma conta)
 *
 * Se conecta com a APIListener (que ouve a resposta da API no momento do login do usuário)
 *
 * Se conecta com o repositório base -> BaseRepository (repositório que serve de base para os repositórios)
 */

// para instanciar na LoginViewModel passo o contexto aqui - recebe heranças  de BaseRepository - contexto aq p acessar as heranças de BaseRepository
class PersonRepository(context: Context): BaseRepository(context) {

    // chama/acessa o serviço (PersonService) através do Retrofit
    private val remote = RetrofitClient.getService(PersonService::class.java)

    // faz chamada à API - recebe da viewmodel as infos de login do usuário e o retorno da APIListener
    fun login(email: String, password: String, listener: APIListener<PersonModel>) {
        val call = remote.login(email, password)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }
}