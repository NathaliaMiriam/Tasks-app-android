package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
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
 * Se conecta com a PersonModel, que mapeia as infos retornadas, as infos que chegam do usuário, quando o mesmo faz login ou criação de uma conta
 *
 * Se conecta com a APIListener (que ouve a resposta da API no momento do login do usuário)
 */

class PersonRepository {

    // chama/acessa o serviço (PersonService) através do Retrofit
    private val remote = RetrofitClient.getService(PersonService::class.java)

    // recebe da viewmodel as infos de login do usuário e o retorno da APIListener
    fun login(email: String, password: String, listener: APIListener<PersonModel>) {
        val call = remote.login(email, password)
        // 'enqueue' coloca a chamada 'remote' na fila - 'callback' chama um trecho de código depois que algo é executado - 'PersonModel' é o que retorna
        call.enqueue(object : Callback<PersonModel>{

            // chamadas de login com e-mail e senha criados no postman:

            // sucesso - a chamada foi e voltou - contudo, é preciso ver o que tem dentro da chamada (pode ser 200 ou 500 ...) ou seja, nem sempre volta com algo positivo
            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                // código p retornar o sucesso
                if (response.code() == 200) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(response.errorBody()!!.string())
                }
            }
            // erro - de comunicação, não tratado, no carater de exceção ...
            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                // código p retornar o erro - mensagem de erro
                listener.onFailure("ERROR_UNEXPECTED")
            }

        })
    }
}