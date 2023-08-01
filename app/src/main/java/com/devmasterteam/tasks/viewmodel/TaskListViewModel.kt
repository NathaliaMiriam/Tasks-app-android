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
 *
 * Se conecta com a AllTasksFragment
 *
 * Se conecta com os repositórios: TaskRepository, PriorityRepository
 */

class TaskListViewModel(application: Application) : AndroidViewModel(application) { // herda de AndroidViewModel p obter o contexto e fazer a instancia abaixo

    private val taskRepository = TaskRepository(application.applicationContext) // faz a instancia do repositório

    private val priorityRepository = PriorityRepository(application.applicationContext) // faz a instancia do repositório p me dar acesso ao banco


    // atribui p o Fragment -> AllTasksFragment o retorno obtido (sucesso ou falha) ref. ao retorno da lista de todas as tarefas atualizada
    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    // atribui p o Fragment -> AllTasksFragment o retorno obtido (sucesso ou falha) ref. a remoção de uma tarefa de acordo c o seu id
    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    // atribui p o Fragment -> AllTasksFragment o retorno obtido (sucesso ou falha) ref. a marcação se a tarefa está completa ou incompleta de acordo c o seu id e status
    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status


    // chama/traz a lista de todas as tarefas atualizada
    fun list() {
        taskRepository.list(object : APIListener<List<TaskModel>>{

            // sucesso - preenche o 'priorityDescription' (descrição da prioridade) usando o banco de dados
            override fun onSuccess(result: List<TaskModel>) {
                result.forEach { // acessa cada um dos elementos, os elementos são referenciados como 'it'
                    it.priorityDescription = priorityRepository.getDescription(it.priorityId) // busca a info, associa p o elemento 'it', acessa e dá o valor
                }
                _tasks.value = result // retorna a lista de todas as tarefas atualizada
            }

            // falha - nada acontece
            override fun onFailure(message: String) {}
        })
    }


    // para deletar uma tarefa de acordo c o seu id
    fun delete(id: Int) {
        taskRepository.delete(id, object : APIListener<Boolean>{

            // sucesso - acontece a remoção e mostra a lista de tarefas atualizada
            override fun onSuccess(result: Boolean) {
                list()
            }

            // falha - não acontece a remoção e instancia a ValidationModel c a mensagem de erro
            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }
        })
    }


    // para marcar o status de uma tarefa como completa ou incompleta de acordo c o seu id
    fun status(id: Int, complete: Boolean) {

        val listener = object : APIListener<Boolean> {

            // sucesso - mostra a lista de tarefas atualizada
            override fun onSuccess(result: Boolean) {
                list()
            }

            // falha - instancia a ValidationModel c a mensagem de erro
            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }
        }

        // se o complete for marcado como verdade ... chama a fun 'complete' passando o id e o listener
        if (complete) {
            taskRepository.complete(id, listener)
            // caso contrário ... chama a fun 'undo' passando o id e o listener
        } else {
            taskRepository.undo(id, listener)
        }
    }

}