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
 * Se conecta com o repositório base -> BaseRepository (repositório que serve de base para os repositórios)
 *
 */

// para instanciar na TaskFormViewModel passo o contexto aqui - recebe heranças de BaseRepository - contexto aq p acessar as heranças de BaseRepository
class TaskRepository(context: Context): BaseRepository(context) {

    // chama/acessa o serviço (TaskService) através do Retrofit
    private val remote = RetrofitClient.getService(TaskService::class.java)


    // prepara a chamada da lista de tarefas -> TaskService
    fun list(listener: APIListener<List<TaskModel>>) { // o listener ouve, pois depois será retornado p a viewModel
        val call = remote.list() // 'list()' está na TaskService
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // prepara a chamada da lista de tarefas dentro de período de sete dias -> TaskService
    fun listNext(listener: APIListener<List<TaskModel>>) { // o listener ouve, pois depois será retornado p a viewModel
        val call = remote.listNext() // 'listNext()' está na TaskService
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // prepara a chamada da lista de tarefas expiradas -> TaskService
    fun listOverdue(listener: APIListener<List<TaskModel>>) { // o listener ouve, pois depois será retornado p a viewModel
        val call = remote.listOverdue() // 'listOverdue()' está na TaskService
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // responsável por fazer a inserção das tarefas na API - o 'listener' informa quem chamou (no caso, a 'TaskFormViewModel')
    fun create(task: TaskModel, listener: APIListener<Boolean>) {
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // responsável por chamar a função delete() - o listener retorna a informação
    fun delete(id: Int, listener: APIListener<Boolean>) {
        val call = remote.delete(id)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }

}