package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
 * Se conecta com o repositório base -> BaseRepository (repositório que serve de base para os repositórios)
 *
 */

// para instanciar na LoginViewModel passo o contexto aqui - recebe heranças de BaseRepository - contexto aq p acessar as heranças de BaseRepository
class PriorityRepository(context: Context): BaseRepository(context) {

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
    // o listener é chamado p fazer o caminho de volta ... Ida : LoginViewModel -> PriorityRepository | Volta: PriorityRepository -> LoginViewModel
    @RequiresApi(Build.VERSION_CODES.M)
    fun list(listener: APIListener<List<PriorityModel>>) {

        // primeiro verifica se existe ou não conexão com a internet
        if (!isConnectionAvailable()) { // se não houver conexão com a internet...
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))// uma mensagem é mostrada ao usuário ... mensagem buscada no xml de strings
            return // e a execução é quebrada
        }

        val call = remote.list()
        executeCall(call, listener) // 'executeCall()' está na 'BaseRepository', criada p simplificar o código
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