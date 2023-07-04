package com.devmasterteam.tasks.service.model

/**
 * Classe criada para ser observada, para atribuir e agrupar os status (positivo | negativo) de login ...
 *
 * Utilizada na LoginActivity e LoginViewModel
 */

class ValidationModel(message: String = "") { // recebe uma mensagem vazia

    // essa var sempre será positiva - se refere ao login de sucesso
    private var status: Boolean = true

    // essa var se refere ao login sem sucesso
    private var validationMessage: String = "" // também é vazia, mas no 'init' ali embaixo a validationMessage assume a message

    init {
        // se a message for diferente de vazio, significa que aparecerá a messagem de erro ao usuário
        if (message != "") {
            validationMessage = message
            status = false // o login de sucesso deixa de ser positivo
        }

    }

    // aqui retorna a var de login de sucesso - fun chamada na LoginActivity
    fun status() = status

    // aqui retorna a var da messagem de login de não sucesso - fun chamada na LoginActivity
    fun message() = validationMessage

}