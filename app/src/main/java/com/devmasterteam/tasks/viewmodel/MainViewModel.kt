package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // instância o SharedPreferences
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    // implementa o logout
    fun logout() {
        // a fun 'remove' faz a remoção dos dados - recebe uma chave, a mesma chave do login -> LoginViewModel
        securityPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        securityPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
        securityPreferences.remove(TaskConstants.SHARED.PERSON_NAME)
    }
}