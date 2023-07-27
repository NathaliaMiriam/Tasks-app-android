package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositório que serve de base para os demais repositórios, ou seja, eles herdam daqui o que for necessário ...
 *
 * O 'Response' bate com 'APIListener', ou seja, o tipo que vai dentro deles bate com o q é retornado p viewModel (p quem chama o repositório)...
 * por esta razão é possível que os dois sejam do tipo 'T'
 *
 */

// 'open' é para a herança, permite que outras classes herdem - contexto colocado p os que herdam poderem acessar
open class BaseRepository(val context: Context) {

    // converte o json recebido c a biblioteca gson ... Toda API converte o json
    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }


    // criada p simplificar o código e ser passada à algumas funções dos repositórios, pois elas contém o mesmo corpo e não teria pq ficar repetindo
    // recebe uma chamada Call - tipo 'T' é para códigos genéricos, é só um tipo que é passado pq tem que ser passado um tipo
    fun <T> executeCall(call: Call<T>, listener: APIListener<T>) { // o 'listener' informa quem chamou -> 'TaskFormViewModel', 'TaskRepository'
        // 'enqueue' coloca a chamada 'remote' na fila - 'Callback' chama um trecho de código depois que algo é executado
        call.enqueue(object : Callback<T> {

            // sucesso - a chamada foi e voltou
            override fun onResponse(call: Call<T>, response: Response<T>) {
                // código p retornar o sucesso - 'TaskConstants.HTTP.SUCCESS' traz o 200 salvo lá na constante 'TaskConstants'
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string())) // recebe o json convertido da 'failResponse' ali de cima
                }
            }

            // falha - de comunicação, não tratada, no carater de exceção ... mostra a mensagem de erro
            override fun onFailure(call: Call<T>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED)) // puxa a string (mensagem) do arq de strings
            }

        })
    }

}