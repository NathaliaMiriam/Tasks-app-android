package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Mapeia os endpoints - Utiliza os métodos/url's de acordo com a DevMasterTeam
 *
 * O 'Call' é a ajuda do Retrofit p fazer a chamada p o endpoint
 *
 * Faz conexão com a TaskModel (que baixa as tarefas da API e converte num objeto)
 *
 * o 'Path' é utilizado para informar ao Retrofit uma concatenação ... neste caso, informa a concatenação do valor que chegará por parametro e a url
 * dentro dele é colocado o caminho (value = valor da var que será trocada | encoded = config. q acrescenta ou não algo no espaço vazio entre dois caracteres)
 *
 * o 'encoded' faz a codificação, ele codifica, ou seja, ele acrescenta algo como /, &, |, % etc no espaço vazio entre dois caracteres, ele substitui o espaço ...
 * isso é feito para evitar erros, como numa url p ex. q não pode haver espaços e tudo precisa estar colado
 *
 */


interface TaskService {

    // lista todas as tarefas sem filtro
    @GET("Task") // url que chama a função -> chamada HTTP
    fun list(): Call<List<TaskModel>>

    // lista todas as tarefas dentro de período de sete dias
    @GET("Task/Next7Days") // url que chama a função -> chamada HTTP
    fun listNext(): Call<List<TaskModel>>

    // lista todas as tarefas expiradas
    @GET("Task/Overdue") // url que chama a função -> chamada HTTP
    fun listOverdue(): Call<List<TaskModel>>


    // busca/carrega apenas 1 tarefa de acordo com o seu id
    @GET("Task/{id}") // url que chama a função + o valor que recebe por parametro (id) ... concatenação -> chamada HTTP
    fun load(@Path(value = "id", encoded = true) id: Int): Call<TaskModel>


    // envia a tarefa
    @POST("Task") // url que chama a função -> chamada HTTP
    @FormUrlEncoded // informa o envio de infos no corpo 'body' da requisição
    fun create(
        // mapeamento dos valores que precisam ser enviados:
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Call<Boolean> // retorna um Booleano


    // atualiza a tarefa
    @PUT("Task") // url que chama a função -> chamada HTTP
    @FormUrlEncoded // informa o envio de infos no corpo 'body' da requisição
    fun update(
        // mapeamento dos valores que precisam ser atualizados:
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Call<Boolean> // retorna um Booleano


    // marca a tarefa como completa
    @PUT("Task/Complete") // url que chama a função -> chamada HTTP
    @FormUrlEncoded // informa o envio de infos no corpo 'body' da requisição
    fun complete(@Field("Id") id: Int): Call<Boolean>


    // marca a tarefa como incompleta
    @PUT("Task/Undo") // url que chama a função -> chamada HTTP
    @FormUrlEncoded // informa o envio de infos no corpo 'body' da requisição
    fun undo(@Field("Id") id: Int): Call<Boolean>


    // deleta a tarefa - usa uma chamada mais genérica de HTTP, passando algumas configs
    @HTTP(method = "DELETE", path = "Task", hasBody = true) // path = url que chama a função -> chamada HTTP | hasBody = temCorpo
    @FormUrlEncoded // informa o envio de infos no corpo 'body' da requisição
    fun delete(@Field("Id") id: Int): Call<Boolean>

}