package com.devmasterteam.tasks.service.listener

/**
 * Pssíveis eventos para cada tarefa
 *
 * O Fragment -> AllTasksFragment é q faz a instancia do TaskListener - é q implementa os métodos de eventos das tarefas
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