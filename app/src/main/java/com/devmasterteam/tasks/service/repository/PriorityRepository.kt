package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositório que orquestra as prioridades
 *
 * Ida : LoginViewModel -> PriorityRepository | Volta: PriorityRepository -> LoginViewModel
 *
 */

class PriorityRepository(val context: Context) {

    // chama/acessa o serviço (PriorityService) através do Retrofit - API
    private val remote = RetrofitClient.getService(PriorityService::class.java)

    // chama/acessa o banco de dados | camada DAO
    private val database = TaskDatabase.getDatabase(context).priorityDAO()


    // o listener é chamado p fazer o caminho de volta ... Ida : LoginViewModel -> PriorityRepository | Volta: PriorityRepository -> LoginViewModel
    fun list(listener: APIListener<List<PriorityModel>>) {
        val call = remote.list()
        // 'enqueue' coloca a chamada 'remote' na fila - 'Callback' chama um trecho de código depois que algo é executado - 'PriorityModel' é o que retorna
        call.enqueue(object : Callback<List<PriorityModel>>{ // <List<PriorityModel>> -> lista de prioridades

            // resposta com sucesso
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                // código p retornar o sucesso - 'TaskConstants.HTTP.SUCCESS' traz o 200 salvo lá na constante TaskConstants
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }

                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string())) // recebe o json convertido da fun failResponse
                }
            }

            // resposta com falha
            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                // código p retornar o erro - mensagem de erro p o usuário
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED)) // puxa a string (mensagem) do arq de strings
            }

        })
    }


    // salva a lista de prioridades no banco de dados
    fun save(list: List<PriorityModel>) {
        database.clear() // 1º limpa a lista para inserir c tudo atualizado
        database.save(list)
    }

    // converte o json recebido c a biblioteca gson ... Toda API converte o json
    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }

}