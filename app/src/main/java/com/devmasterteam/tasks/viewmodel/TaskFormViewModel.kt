package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.PriorityRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    // instancia o repositório
    private val priorityRepository = PriorityRepository(application.applicationContext)

    // var observada na TaskFormActivity
    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList


    // carrega do repositório as prioridades p serem adds no Spinner
    fun loadPriorities() {
        _priorityList.value = priorityRepository.list()
    }

}