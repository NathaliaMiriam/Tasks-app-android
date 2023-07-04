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
 * Orquestra as chamadas para o banco de dados ou API, neste caso, API
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
 */

class PersonRepository(val context: Context) { // com o contexto inserido eu consigo lá embaixo chamar a minha string...

    // chama/acessa o serviço (PersonService) através do Retrofit
    private val remote = RetrofitClient.getService(PersonService::class.java)

    // faz chamada à API - recebe da viewmodel as infos de login do usuário e o retorno da APIListener
    fun login(email: String, password: String, listener: APIListener<PersonModel>) {
        val call = remote.login(email, password)
        // 'enqueue' coloca a chamada 'remote' na fila - 'callback' chama um trecho de código depois que algo é executado - 'PersonModel' é o que retorna
        call.enqueue(object : Callback<PersonModel>{

            // faz o tratamento das chamadas de login com e-mail e senha criados no postman:

            // 1) sucesso - a chamada foi e voltou - contudo, é preciso ver o que tem dentro da chamada (pode ser 200 ou 500 ...) ou seja, nem sempre volta com algo positivo
            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                // código p retornar o sucesso - 'TaskConstants.HTTP.SUCCESS' traz o 200 salvo lá na constante TaskConstants
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string())) // recebe o json convertido da fun failResponse
                }
            }
            // 2) erro - de comunicação, não tratado, no carater de exceção ...
            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                // código p retornar o erro - mensagem de erro p o usuário
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED)) // puxa a string (mensagem) do arq de strings
            }

        })
    }

    // converte o json recebido c a biblioteca gson ... Toda API converte o json
    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }
}