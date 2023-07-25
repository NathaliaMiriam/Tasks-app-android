package com.devmasterteam.tasks.service.listener

/**
 * Eventos possíveis para cada tarefa
 *
 * Se conecta com o Adapter -> TaskAdapter
 *
 * Se conecta com a TaskViewHolder
 */

interface TaskListener {

    // click para edição
    fun onListClick(id: Int)

    // remoção
    fun onDeleteClick(id: Int)

    // completa tarefa
    fun onCompleteClick(id: Int)

    // descompleta tarefa
    fun onUndoClick(id: Int)

}