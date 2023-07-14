package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Response

/**
 * Repositório que serve de base para os demais repositórios, ou seja, eles herdam daqui o que for necessário ...
 *
 * Aqui a responsabilidade da 'failResponse' é centralizada, para que o código não seja duplicado, triplicado...
 *
 * Aqui a responsabilidade da 'handleResponse' é centralizada, para que o código não seja duplicado, triplicado...
 *
 * O 'Response' bate com 'APIListener', ou seja, o tipo que vai dentro deles bate com o q é retornado p viewModel (p quem chama o repositório)...
 * por esta razão é possível que os dois sejam do tipo 'T'
 *
 */

// 'open' é para a herança, permite que outras classe herdem
open class BaseRepository {

    // converte o json recebido c a biblioteca gson ... Toda API converte o json
    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }

    // tipo 'T' é para códigos genéricos, é só um tipo que é passado pq tem que ser passado um tipo
    fun <T> handleResponse(response: Response<T>, listener: APIListener<T>) { // o 'listener' informa quem chamou (no caso, a 'TaskFormViewModel')
        // código p retornar o sucesso - 'TaskConstants.HTTP.SUCCESS' traz o 200 salvo lá na constante TaskConstants
        if (response.code() == TaskConstants.HTTP.SUCCESS) {
            response.body()?.let { listener.onSuccess(it) }
        } else {
            listener.onFailure(failResponse(response.errorBody()!!.string())) // recebe o json convertido da 'failResponse' ali de cima
        }
    }

}