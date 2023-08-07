package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
 * Repositório que orquestra a inserção, remoção, edição das tarefas na API
 *
 * Se conecta com o repositório base -> BaseRepository (repositório que serve de base para os repositórios)
 *
 * Para qualquer ação é feita a checagem de conexão com a internet antes
 *
 */

// para instanciar na TaskFormViewModel passo o contexto aqui - recebe heranças de BaseRepository - contexto aq p acessar as heranças de BaseRepository
class TaskRepository(context: Context): BaseRepository(context) {

    // chama/acessa o serviço (TaskService) através do Retrofit
    private val remote = RetrofitClient.getService(TaskService::class.java)



    // prepara a chamada da lista de tarefas -> a fun e o tipo de retorno <TaskModel> tenho no 'TaskService'
    fun list(listener: APIListener<List<TaskModel>>) { // o listener ouve quem chamou para depois retornar p a viewModel

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.list() // 'list()' está na TaskService
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // prepara a chamada da lista de tarefas dentro de período de sete dias -> a fun e o tipo de retorno <TaskModel> tenho no 'TaskService'
    fun listNext(listener: APIListener<List<TaskModel>>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.listNext() // 'listNext()' está na TaskService
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // prepara a chamada da lista de tarefas expiradas -> a fun e o tipo de retorno <TaskModel> tenho no 'TaskService'
    fun listOverdue(listener: APIListener<List<TaskModel>>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.listOverdue() // 'listOverdue()' está na TaskService
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // prepara a inserção das tarefas na API -> a fun e o tipo de retorno <Boolean> tenho no 'TaskService'
    fun create(task: TaskModel, listener: APIListener<Boolean>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }

    // prepara a edição das tarefas na API -> a fun e o tipo de retorno <Boolean> tenho no 'TaskService'
    fun update(task: TaskModel, listener: APIListener<Boolean>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.update(task.id, task.priorityId, task.description, task.dueDate, task.complete)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }

    // prepara a busca de uma tarefa -> a fun e o tipo de retorno <TaskModel> tenho no 'TaskService'
    fun load(id: Int, listener: APIListener<TaskModel>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.load(id)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }


    // prepara a remoção de uma tarefa -> a fun e o tipo de retorno <Boolean> tenho no 'TaskService'
    fun delete(id: Int, listener: APIListener<Boolean>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.delete(id)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }

    // prepara a marcação de uma tarefa como completa -> a fun e o tipo de retorno <Boolean> tenho no 'TaskService'
    fun complete(id: Int, listener: APIListener<Boolean>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.complete(id)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }

    // prepara a marcação de uma tarefa como incompleta -> a fun e o tipo de retorno <Boolean> tenho no 'TaskService'
    fun undo(id: Int, listener: APIListener<Boolean>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.undo(id)
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
    }

}