package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositório que orquestra as prioridades
 *
 * Se conecta com PriorityDAO
 *
 * Ida : LoginViewModel -> PriorityRepository | Volta: PriorityRepository -> LoginViewModel
 *
 * Cache - funciona para guardar dados, informações e processos temporários acessados com frequência...
 * Pode ser tanto de aplicativos, quanto de navegador...
 * Evita chamadas desnecessárias, deixa mais rápido para o usuário...
 *
 *
 *
 */

// para eu conseguir instanciar lá na LoginViewModel é necessário o contexto aqui - Recebe a herança de BaseRepository
class PriorityRepository(val context: Context): BaseRepository() {

    // chama/acessa o serviço (PriorityService) através do Retrofit
    private val remote = RetrofitClient.getService(PriorityService::class.java)

    // chama/acessa o banco de dados | camada DAO
    private val database = TaskDatabase.getDatabase(context).priorityDAO()


    /**
     * Cache - salva na memória para evitar acesso desnecessário ao banco, priorizando a performance
     */

    // armazena o Cache do id e da descrição das prioridades
    companion object {
        private val cache = mutableMapOf<Int, String>() // dentro coloquei os meus pares: chave e valor -> id e string(descrição)
        fun getDescription(id: Int): String {
            return cache[id] ?: "" // retorna o valor(descrição) associado a chave(id), se não tiver nada, se for nulo, retorna uma string vazia
        }
        fun setDescription(id: Int, str: String) { // seta a descrição - espera chave(id) e valor(descrição)
            cache[id] = str // preenche
        }
    }

     // retorna a descrição das prioridades baseada no id, a ideia é evitar o acesso ao banco, o acesso é p ser só quando houver desconhecimento da descrição
    fun getDescription(id: Int): String {
        // se for vazio, se não tiver valor(descrição) associado a chave(id) ...
        val cached = PriorityRepository.getDescription(id)
        return if (cached == "") {
            // busca no banco a descrição de acordo com o id
            val description = database.getDescription(id)
            PriorityRepository.setDescription(id, description) // passei o id recebido lá em cima e a chave buscada no banco
            description // retorna a descrição
        } else { // caso contrário ...
            cached // acessa novamente o cache
        }
    }


    // retorna da API a lista de prioridades - informo por parametro que existe o listener
    fun list(listener: APIListener<List<PriorityModel>>) {
        // o listener é chamado p fazer o caminho de volta ... Ida : LoginViewModel -> PriorityRepository | Volta: PriorityRepository -> LoginViewModel
        val call = remote.list()
        // 'enqueue' coloca a chamada 'remote' na fila - 'Callback' chama um trecho de código depois que algo é executado - 'PriorityModel' é o que retorna
        call.enqueue(object : Callback<List<PriorityModel>>{ // <List<PriorityModel>> -> lista de prioridades

            // resposta com sucesso
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                handleResponse(response, listener) // fun na BaseRepository
            }

            // resposta com falha
            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                // código p retornar o erro - mensagem de erro p o usuário
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED)) // puxa a string (mensagem) do arq de strings
            }

        })
    }

    // retorna do banco de dados a lista de prioridades - não preciso informar nada por parametro
    fun list(): List<PriorityModel> {
        return database.list()
    }

    // salva a lista de prioridades no banco de dados
    fun save(list: List<PriorityModel>) {
        database.clear() // 1º limpa a lista para inserir c tudo atualizado
        database.save(list)
    }

}