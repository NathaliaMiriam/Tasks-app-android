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
 * Repositório que orquestra a inserção das tarefas na API
 */

// para eu conseguir instanciar lá na TaskFormViewModel é necessário o contexto aqui - Recebe a herança de BaseRepository
class TaskRepository(val context: Context): BaseRepository() {

    // chama/acessa o serviço (TaskService) através do Retrofit
    private val remote = RetrofitClient.getService(TaskService::class.java)


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

}