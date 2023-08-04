package com.devmasterteam.tasks.service.model

/**
 * Classe criada para ser observada, para atribuir e agrupar os status (positivo | negativo) de login, inserção de tarefa, carregamento da tarefa, cadastro de usuário ...
 *
 * Utilizada na LoginActivity, LoginViewModel, TaskFormViewModel ...
 */

class ValidationModel(message: String = "") { // recebe uma mensagem vazia, pois inicialmente é pra se ter sucesso e não mensagem de erro

    // essa var sempre será positiva - se refere ao sucesso
    private var status: Boolean = true

    // essa var se refere a falha
    private var validationMessage: String = "" // também é vazia, mas no 'init' ali embaixo a validationMessage assume a message

    init {
        // se a message for diferente de vazio, significa que deu falha e aparecerá a messagem de erro ao usuário
        if (message != "") {
            validationMessage = message
            status = false // deixa de ser verdadeiro e passa a ser falso
        }

    }

    // aqui retorna a var de sucesso
    fun status() = status

    // aqui retorna a var da messagem de não sucesso
    fun message() = validationMessage

}