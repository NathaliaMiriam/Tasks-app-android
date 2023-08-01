package com.devmasterteam.tasks.service.listener

/**
 * Pssíveis eventos de clique para cada tarefa
 *
 * O Fragment -> AllTasksFragment é q faz a instancia do TaskListener - é q implementa os métodos de eventos das tarefas
 *
 * Se conecta com o Adapter -> TaskAdapter
 *
 * Se conecta com a TaskViewHolder
 */

interface TaskListener {

    // clique para edição
    fun onListClick(id: Int)

    // clique para remoção
    fun onDeleteClick(id: Int)

    // clique para completar a tarefa
    fun onCompleteClick(id: Int)

    // clique para descompletar a tarefa
    fun onUndoClick(id: Int)

}