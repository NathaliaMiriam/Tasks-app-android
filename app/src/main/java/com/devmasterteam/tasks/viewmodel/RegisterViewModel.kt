package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    // instancia PersonRepository
    private val personRepository = PersonRepository(application.applicationContext)

    // instancia a SharedPreferences
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    // atribui p a Activity -> RegisterActivity o retorno obtido (sucesso ou falha) ref. ao cadastro do usuário
    private val _user = MutableLiveData<ValidationModel>() // 'ValidationModel' é a classe que agrupa e atribui os status de cadastro de usuário
    val user: LiveData<ValidationModel> = _user


    // recebe da RegisterActivity as infos que são colocadas pelo usuário p realizar o cadastro

    fun create(name: String, email: String, password: String) {
        // chama a fun do repositório
        personRepository.create(name, email, password, object : APIListener<PersonModel>{

            // sucesso - obtém os dados de cadastro do usuário e os salva
            override fun onSuccess(result: PersonModel) {

                // salva na SecurityPreferences os dados de cadastro do usuário, após a captura -- 'store()' é a fun da SecurityPreferences que salva dados
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                // add os headers no RetrofitClient
                RetrofitClient.addHeaders(result.token, result.personKey)
            }

            // falha - notifica o usuário sobre o erro com as vars que podem ser observadas
            override fun onFailure(message: String) {
                _user.value = ValidationModel(message)
            }
        })
    }

}