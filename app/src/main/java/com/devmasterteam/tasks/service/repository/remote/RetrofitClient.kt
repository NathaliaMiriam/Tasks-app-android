package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit -> simplifica as chamadas à API
 *
 * Faz conexão com as infos do usuário -> PersonService
 *
 * Utiliza como base a DevMasterTeam
 *
 * Faz conexão com o repositório -> PersonRepository
 *
 * O 'httpClient' é quem faz as requisições, o Retrofit passa algumas infos/configs p ele e posteriormente ele é construído...
 * O 'addInterceptor' intercepta a requisição...
 * ... coloca uma barreira (como um pedágio, por ex.) no httpClient, e quando estiver passando, o httpClient recebe algumas coisas do addInterceptor e posteriormente continua
 *
 * O Header pode ser passado vazio
 *
 */

class RetrofitClient private constructor() {
    companion object {

        private lateinit var INSTANCE: Retrofit

        // valores do header
        private var token: String = ""
        private var personKey: String = ""

        // função acessível para retornar os serviços do Retrofit
        private fun getRetrofitInstance(): Retrofit {

            val httpClient = OkHttpClient.Builder()

            // instancia o httpClient c/ o interceptor 'addInterceptor'
            httpClient.addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response { // o 'intercept' retorna uma resposta e dentro tenho uma cadeia de conexão
                    val request = chain.request() // requisição da cadeia de conexão ... é barrada antes de prosseguir, para que seja acrescentado coisas nela, como o header p ex.
                        .newBuilder()
                        // dentro dos headers é colocado nome e valor do header q quero passar ...
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, token)
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .build() // fecha as coisas

                    return chain.proceed(request) // diz que pode continuar a requisição
                }

            })

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

        // retorna um serviço, a instancia de qualquer serviço -> de PersonService ...
        fun <T> getService(serviceClass: Class<T>): T { // tipo <T> é para códigos genéricos...
            return getRetrofitInstance().create(serviceClass) // ou seja, aqui posso retornar qualquer tipo de serviço
        }

        // add os valores dos headers no 'intercept' ali em cima
        fun addHeaders(tokenValue: String, personKeyValue: String) {
            token = tokenValue
            personKey = personKeyValue
        }
    }

}