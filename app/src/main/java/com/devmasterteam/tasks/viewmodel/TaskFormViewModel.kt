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

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    // instancia o repositório
    private val priorityRepository = PriorityRepository(application.applicationContext)

    // instancia o repositório
    private val taskRepository = TaskRepository(application.applicationContext)

    // var observada na TaskFormActivity
    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    // var observada pela TaskFormActivity na fun observe() - 'ValidationModel' é a classe que agrupa e atribui os status de inserção de tarefa
    private val _taskSave = MutableLiveData<ValidationModel>()
    val taskSave: LiveData<ValidationModel> = _taskSave


    // para salvar na API o agrupamento dos campos preenchidos p criar/add as tarefas - pega e manda para o repositório - retorna p Activity o sucesso ou a falha
    fun save(task: TaskModel) {
        taskRepository.create(task, object : APIListener<Boolean>{ // fun 'create' na TaskRepository

            // sucesso
            override fun onSuccess(result: Boolean) {
                _taskSave.value = ValidationModel() // instancia sem nada no contrutor, pois deu sucesso
            }

            // falha
            override fun onFailure(message: String) {
                _taskSave.value = ValidationModel(message) // instancia c a mensagem no contrutor, pois deu falha
            }
        })
    }


    // carrega do repositório as prioridades p serem adds no Spinner
    fun loadPriorities() {
        _priorityList.value = priorityRepository.list()
    }

}