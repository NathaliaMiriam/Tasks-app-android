package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.PersonModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Serviço de informações do usuário que usa o Retrofit
 *
 * 4) Daqui retorna a PersonModel
 *
 * Faz conexão com o Retrofit -> RetrofitClient
 *
 * Utiliza os métodos/url's da DevMasterTeam
 *
 * Faz conexão com a PersonModel (que mapeia as informações que chegam do usuário)
 *
 */

interface PersonService {

    // usuário logado:

    // O 'POST' faz a postagem
    @POST("Authentication/Login") // a url 'Authentication/Login' é concatenada c a url base quando o Retrofit estiver fazendo a chamada
    @FormUrlEncoded // para enviar as infos do corpo (e-mail e senha)

    // o @Field sinaliza p o Retrofit as infos q serão enviadas - o Call ajuda a fazer uma chamada p o Retrofit - PersonModel é o retorno
    fun login(@Field("email") email: String,
              @Field("password") password: String): Call<PersonModel>


    // usuário criado:

    @POST("Authentication/Create") // a url 'Authentication/Login' é concatenada c a url base quando o Retrofit estiver fazendo a chamada
    @FormUrlEncoded // para enviar as infos do corpo (nome, e-mail e senha)

    // o @Field sinaliza p o Retrofit as infos q serão enviadas - o Call ajuda a fazer uma chamada p o Retrofit - PersonModel é o retorno
    fun create(@Field("name") name: String,
               @Field("email") email: String,
               @Field("password") password: String): Call<PersonModel>


}