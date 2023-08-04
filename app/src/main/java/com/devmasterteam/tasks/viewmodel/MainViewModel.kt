package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // instância o SharedPreferences
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    // atribui p a Activity -> MainActivity o retorno obtido ref. ao nome do usuário no menu do app
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name


    // implementa o logout
    fun logout() {
        // a fun 'remove' faz a remoção dos dados - recebe uma chave, a mesma chave do login -> LoginViewModel
        securityPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        securityPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
        securityPreferences.remove(TaskConstants.SHARED.PERSON_NAME)
    }

    // busca/carrega o nome do usuário p salvar no menu do app
    fun loadUserName() {
        _name.value = securityPreferences.get(TaskConstants.SHARED.PERSON_NAME)
    }
}