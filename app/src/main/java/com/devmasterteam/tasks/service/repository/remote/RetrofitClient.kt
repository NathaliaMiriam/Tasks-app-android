package com.devmasterteam.tasks.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit -> simplifica as chamadas à API
 *
 * Faz conexão com a interface do usuário -> PersonService
 *
 * Utilizei como base a DevMasterTeam
 */

class RetrofitClient private constructor() {
    companion object {

        private lateinit var INSTANCE: Retrofit

        // função acessível para retornar os serviços do Retrofit
        private fun getRetrofitInstance(): Retrofit {

            val httpClient = OkHttpClient.Builder()

            // verifica se o INSTANCE não foi inicializado, se não tiver sido, será inicializado ali embaixo...
            if (!::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class) { // faz o synchronized p que só uma thread entre no código
                    INSTANCE = Retrofit.Builder() // faz a instancia
                        .baseUrl("http://devmasterteam.com/CursoAndroidAPI/") // url base da API - que concatena com a url do método utilizado p o usuário
                        .client(httpClient.build()) // osquestra as chamadas http
                        .addConverterFactory(GsonConverterFactory.create()) // converte o JSON p as classes (biblioteca gson)
                        .build() // inicializa o INSTANCE caso ele não tenha sido inicializado ali em cima
                }
            }
            return INSTANCE // retorna a instancia
        }

    }

}