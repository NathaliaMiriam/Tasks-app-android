package com.devmasterteam.tasks.service.listener

/**
 * Ouve a resposta da API no momento do login do usuário
 *
 * Prepara a aplicação para responder de diferentes maneiras, ou seja, diferentes respostas p o mesmo método
 *
 * Se conecta com a viewmodel -> LoginViewModel
 *
 * Se conecta com o repositório -> PersonRepository
 *
 */

// recebe um tipo <T> que é para códigos genéricos...
interface APIListener<T> {

    // p login bem sucedido - retorna um tipo <T> lá no repositório, na fun login, com o que eu quiser
    fun onSuccess(result: T) {}

    // p login mau sucedido - retorna uma string lá no repositório, na fun login
    fun onFailure(message: String) {}
}