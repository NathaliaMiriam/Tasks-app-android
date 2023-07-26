package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

/**
 * Faz a chamada à API e retorna as informações
 */

class TaskListViewModel(application: Application) : AndroidViewModel(application) { // herda de AndroidViewModel p obter o contexto e fazer a instancia abaixo

    private val taskRepository = TaskRepository(application.applicationContext) // faz a instancia do repositório

    private val priorityRepository = PriorityRepository(application.applicationContext) // faz a instancia do repositório p me dar acesso ao banco

    // atribui p a fragment (AllTasksFragment) o retorno obtido (sucesso ou falha)
    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    // chama/traz a lista de todas as tarefas
    fun list() {
        taskRepository.list(object : APIListener<List<TaskModel>>{

            // sucesso - preenche o 'priorityDescription' (descrição da prioridade) usando o banco de dados
            override fun onSuccess(result: List<TaskModel>) {
                result.forEach { // acessa cada um dos elementos, os elementos são referenciados como 'it'
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId) // busca a info, associa p o elemento 'it', acessa e dá o valor
                }
                _tasks.value = result // retorna a lista de todas as tarefas
            }

            // falha
            override fun onFailure(message: String) {}
        })
    }

}