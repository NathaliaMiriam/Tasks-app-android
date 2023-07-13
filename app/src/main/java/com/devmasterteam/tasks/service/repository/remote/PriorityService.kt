package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.PriorityModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Lista de prioridades -> "Baixa - id: 1" | "Média - id: 2" | "Alta - id: 3" | "Crítica - id: 4"
 * Quando a lista de tarefas for carregada no banco de dados a info sobre a prioridade estará nela
 *
 * Utiliza os métodos/url's de acordo com a DevMasterTeam
 *
 * o 'GET' faz a busca | o 'Call' é a ajuda do Retrofit p fazer a chamada p o endpoint
 *
 * Faz conexão com a PriorityModel (que mapeia os dados que chegam da API e diz como os dados serão salvos no banco de dados)
 */

interface PriorityService {

    @GET("Priority") // url que chama a função
    fun list(): Call<List<PriorityModel>> // listagem de tudo

}