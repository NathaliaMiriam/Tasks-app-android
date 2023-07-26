package com.devmasterteam.tasks.service.listener

/**
 * Ouve as respostas da API
 *
 * Prepara a aplicação para responder de diferentes maneiras, ou seja, diferentes respostas p o mesmo método
 *
 * Se conecta com as viewmodel
 *
 * Se conecta com os repositórios
 *
 */

// recebe um tipo <T> que é para códigos genéricos...
interface APIListener<T> {

    // p os sucessos
    fun onSuccess(result: T) {}

    // p as falhas
    fun onFailure(message: String) {}
}