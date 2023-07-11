package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.PriorityModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Lista de prioridades -> "Baixa - id: 1" | "Média - id: 2" | "Alta - id: 3" | "Crítica - id: 4"
 * Quando a lista de tarefas for carregada no banco de dados a info sobre a prioridade estará nela
 *
 * Utiliza os métodos/url's da DevMasterTeam
 */

interface PriorityService {

    // O 'GET' faz a busca, dentro dele coloco a URL que consta na API
    @GET("Priority")
    fun list(): Call<List<PriorityModel>> // o 'Call' é a ajuda do Retrofit p fazer a chamada p o endpoint

}