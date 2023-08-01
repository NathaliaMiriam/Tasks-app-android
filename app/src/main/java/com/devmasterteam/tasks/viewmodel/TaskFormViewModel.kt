package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

/**
 * Se conecta com a TaskFormActivity
 *
 * Se conecta com os repositórios: PriorityRepository, TaskRepository
 */

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    // instancia o repositório
    private val priorityRepository = PriorityRepository(application.applicationContext)

    // instancia o repositório
    private val taskRepository = TaskRepository(application.applicationContext)


    // atribui p a Activity -> TaskFormActivity o retorno obtido (sucesso ou falha)
    private val _priorityList = MutableLiveData<List<PriorityModel>>() // 'PriorityModel' é o retorno
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    // atribui p a Activity -> TaskFormActivity o retorno obtido (sucesso ou falha)
    private val _taskSave = MutableLiveData<ValidationModel>() // 'ValidationModel' é a classe que agrupa e atribui os status de inserção de tarefa
    val taskSave: LiveData<ValidationModel> = _taskSave

    // atribui p a Activity -> TaskFormActivity o retorno obtido (sucesso) ref. ao carregamento das infos da tarefa
    private val _task = MutableLiveData<TaskModel>() // 'TaskModel' é o retorno
    val task: LiveData<TaskModel> = _task

    // atribui p a Activity -> TaskFormActivity o retorno obtido (falha) ref. ao carregamento das infos da tarefa
    private val _taskLoad = MutableLiveData<ValidationModel>() // 'ValidationModel' é a classe que agrupa e atribui os status de carregamento da tarefa
    val taskLoad: LiveData<ValidationModel> = _taskLoad


    // para salvar na API o agrupamento dos campos preenchidos p criar/add as tarefas - pega e manda para o repositório - retorna p Activity o sucesso ou a falha
    fun save(task: TaskModel) {

        val listener = object : APIListener<Boolean>{

            // sucesso - instancia a ValidationModel sem nada no contrutor
            override fun onSuccess(result: Boolean) {
                _taskSave.value = ValidationModel()
            }

            // falha - instancia a ValidationModel c a mensagem de erro no contrutor
            override fun onFailure(message: String) {
                _taskSave.value = ValidationModel(message)
            }
        }

        if (task.id == 0) { // se o id for 0, faz a criação
            taskRepository.create(task, listener)
        } else { // caso contrário, faz a edição
            taskRepository.update(task, listener)
        }
    }


    // busca/carrega a tarefa e suas infos através do id e passa p a TaskFormActivity
    fun load(id: Int) {
        taskRepository.load(id, object : APIListener<TaskModel>{

            // sucesso - a tarefa carrega e as suas infos são retornadas para a Activity
            override fun onSuccess(result: TaskModel) {
                _task.value = result
            }

            // falha - tarefa não carrega por algum motivo e instancia a ValidationModel c a mensagem de erro
            override fun onFailure(message: String) {
                _taskLoad.value = ValidationModel(message)
            }
        })
    }


    // busca/carrega do repositório as prioridades p serem adds no Spinner lá na 'TaskFormActivity'
    fun loadPriorities() {
        _priorityList.value = priorityRepository.list()
    }

}