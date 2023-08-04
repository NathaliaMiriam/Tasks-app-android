package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
 * Se conecta com a LoginViewModel
 *
 * Se conecta com o serviço (PersonService) para verificar se as infos do usuário estão ok
 *
 * Se conecta com a PersonModel (que mapeia as infos retornadas, as infos que chegam do usuário, quando o mesmo faz login ou criação de uma conta)
 *
 * Se conecta com a APIListener (que ouve a resposta da API no momento do login do usuário)
 *
 * Se conecta com o repositório base -> BaseRepository (repositório que serve de base para os repositórios)
 *
 * Se conecta com a RegisterViewModel
 */

// para instanciar na LoginViewModel passo o contexto aqui - recebe heranças de BaseRepository - contexto aq p acessar as heranças de BaseRepository
class PersonRepository(context: Context): BaseRepository(context) {

    // chama/acessa o serviço (PersonService) através do Retrofit
    private val remote = RetrofitClient.getService(PersonService::class.java)


    // passa p à API as infos de login do usuário e o retorno da APIListener
    @RequiresApi(Build.VERSION_CODES.M)
    fun login(email: String, password: String, listener: APIListener<PersonModel>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION)) // uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.login(email, password)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // passa p à API as infos de cadastro do usuário
    @RequiresApi(Build.VERSION_CODES.M)
    fun create(name: String, email: String, password: String, listener: APIListener<PersonModel>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION)) // uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.create(name, email, password)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }
}