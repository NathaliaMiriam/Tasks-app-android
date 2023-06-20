package com.devmasterteam.tasks.service.listener

/**
 * Eventos para a listagem de tarefas
 */

interface TaskListener {

    /**
     * Click para edição
     */
    fun onListClick(id: Int)

    /**
     * Remoção
     */
    fun onDeleteClick(id: Int)

    /**
     * Completa tarefa
     */
    fun onCompleteClick(id: Int)

    /**
     * Descompleta tarefa
     */
    fun onUndoClick(id: Int)

}