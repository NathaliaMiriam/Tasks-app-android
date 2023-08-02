package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repositório que serve de base para os demais repositórios, ou seja, eles herdam daqui o que for necessário ...
 *
 * O 'Response' bate com 'APIListener', ou seja, o tipo que vai dentro deles bate com o q é retornado p viewModel (p quem chama o repositório)...
 * por esta razão é possível que os dois sejam do tipo 'T'
 *
 */

// 'open' é para a herança, permite que outras classes herdem - contexto colocado p os que herdam poderem acessar
open class BaseRepository(val context: Context) {

    // converte o json recebido c a biblioteca gson ... Toda API converte o json
    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }


    // criada p simplificar o código e ser passada à algumas funções dos repositórios, pois elas contém o mesmo corpo e não teria pq ficar repetindo
    // recebe uma chamada Call - tipo 'T' é para códigos genéricos, é só um tipo que é passado pq tem que ser passado um tipo
    fun <T> executeCall(call: Call<T>, listener: APIListener<T>) { // o 'listener' informa quem chamou
        // 'enqueue' coloca a chamada 'remote' na fila - 'Callback' chama um trecho de código depois que algo é executado
        call.enqueue(object : Callback<T> {

            // sucesso - a chamada foi e voltou
            override fun onResponse(call: Call<T>, response: Response<T>) {
                // código p retornar o sucesso - 'TaskConstants.HTTP.SUCCESS' traz o 200 salvo lá na constante 'TaskConstants'
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string())) // recebe o json convertido da 'failResponse' ali de cima
                }
            }

            // falha - de comunicação, não tratada, no carater de exceção ... mostra a mensagem de erro
            override fun onFailure(call: Call<T>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED)) // puxa a string (mensagem) do arq de strings
            }

        })
    }


    // verifica se a conexão com a internet está disponivel
    @RequiresApi(Build.VERSION_CODES.M)
    fun isConnectionAvailable(): Boolean {

        // para assumir que não existe coneção com a internet
        var result = false

        // pega o serviço do sistema e converte - 'getSystemService' pega sistemas do Android - 'ConnectivityManager' é um gerenciador de conexões q consegue me informar algumas coisas
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // versões mais novas do Android usam o código abaixo
        // se o Android tiver uma API de 23 p cima, o código abaixo é usado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 'VERSION.SDK_INT' pega a versão do Android, retona a API utilizada - 'M' é a API 23 ('Ctrl + B' verifica)
            // com o uso do gerenciador de conexões verifica se existe uma rede de internet ativa, se tiver, pega as funcionalidades da rede ... cód. disp. a partir da API 23
            val activeNet = cm.activeNetwork ?: return false
            // verifica/pega as funcionalidades da rede - 'getNetworkCapabilities' pega as funcionalidades/capacidades da rede
            val netWorkCapabilities = cm.getNetworkCapabilities(activeNet) ?: return false

            result = when { // quando as funcionalidades da rede forem verdade ... se tiver algum dos dois abaixo, significa que tem conexão
                netWorkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                netWorkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false // caso contrário, caso não tenha nenhum dos dois acima, significa que não tem conexão
            }
            // versões mais antigas do Android usam o código abaixo
            // caso contrário, se o Android não tiver uma API de 23 p cima, o código abaixo é usado - os códigos estão sublinhados pq eventualmente podem morrer
        } else {
            if (cm.activeNetworkInfo != null) {
                result = when (cm.activeNetworkInfo!!.type) {
                    // 3 tipos de verificações feitas abaixo do Android 23
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return result // retorna o que eu obtive -> true (conexão) | false (sem conexão)
    }

}