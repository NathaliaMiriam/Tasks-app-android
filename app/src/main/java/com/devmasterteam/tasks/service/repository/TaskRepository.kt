package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositório que orquestra a inserção, remoção das tarefas na API
 *
 */

// para eu conseguir instanciar lá na TaskFormViewModel é necessário o contexto aqui - Recebe a herança de BaseRepository
class TaskRepository(val context: Context): BaseRepository() {

    // chama/acessa o serviço (TaskService) através do Retrofit
    private val remote = RetrofitClient.getService(TaskService::class.java)


    // prepara a chamada da lista de tarefas - TaskService
    fun list(listener: APIListener<List<TaskModel>>) { // o listener ouve, pois depois será retornado p a viewModel
        val call = remote.list() // 'list()' está na TaskService
        list(call, listener) // 'list()' está logo ali embaixo, criada p simplificar o código
    }


    // prepara a chamada da lista de tarefas dentro de período de sete dias - TaskService
    fun listNext(listener: APIListener<List<TaskModel>>) { // o listener ouve, pois depois será retornado p a viewModel
        val call = remote.listNext() // 'listNext()' está na TaskService
        list(call, listener) // 'list()' está logo ali embaixo, criada p simplificar o código
    }


    // prepara a chamada da lista de tarefas expiradas - TaskService
    fun listOverdue(listener: APIListener<List<TaskModel>>) { // o listener ouve, pois depois será retornado p a viewModel
        val call = remote.listOverdue() // 'listOverdue()' está na TaskService
        list(call, listener) // 'list()' está logo ali embaixo, criada p simplificar o código
    }


    // fun criada para simplificar as chamadas das funções list, listNext e listOverdue, pois o corpo é o mesmo, a partir do 'remote' o retorno é o mesmo
    private fun list(call: Call<List<TaskModel>>, listener: APIListener<List<TaskModel>>) { // o 'Call' é o mesmo que está no serviço (TaskService)
        // 'enqueue' coloca a chamada 'remote' na fila - 'Callback' chama um trecho de código depois que algo é executado - 'TaskModel' é o que retorna
        call.enqueue(object : Callback<List<TaskModel>>{

            // sucesso
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                handleResponse(response, listener) // fun na BaseRepository
            }

            // falha
            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                // código p retornar o erro - mensagem de erro p o usuário
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED)) // puxa a string (mensagem) do arq de strings
            }
        })
    }


    // responsável por fazer a inserção das tarefas na API - o 'listener' informa quem chamou (no caso, a 'TaskFormViewModel')
    fun create(task: TaskModel, listener: APIListener<Boolean>) {
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        // 'enqueue' coloca a chamada 'remote' na fila - 'Callback' chama um trecho de código depois que algo é executado - 'Boolean' é o que retorna
        call.enqueue(object : Callback<Boolean>{

            // sucesso
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                handleResponse(response, listener) // fun na BaseRepository
            }

            // falha
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                // código p retornar o erro - mensagem de erro p o usuário
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED)) // puxa a string (mensagem) do arq de strings
            }
        })
    }


    // responsável por chamar a função delete() - o listener retorna a informação
    fun delete(id: Int, listener: APIListener<Boolean>) {
        val call = remote.delete(id)
        // 'enqueue' coloca a chamada 'remote' na fila - 'Callback' chama um trecho de código depois que algo é executado - 'Boolean' é o que retorna
        call.enqueue(object : Callback<Boolean>{

            // sucesso
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                handleResponse(response, listener) // fun na BaseRepository
            }

            // falha
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                // código p retornar o erro - mensagem de erro p o usuário
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED)) // puxa a string (mensagem) do arq de strings
            }
        })
    }

}