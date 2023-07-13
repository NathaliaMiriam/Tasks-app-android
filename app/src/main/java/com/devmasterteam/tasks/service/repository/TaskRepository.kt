package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService

class TaskRepository {

    // chama/acessa o serviço (TaskService) através do Retrofit
    private val remote = RetrofitClient.getService(TaskService::class.java)


}