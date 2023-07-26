package com.devmasterteam.tasks.service.model

import com.google.gson.annotations.SerializedName

/**
 * Baixa as tarefas da API e converte num objeto
 *
 * Utiliza como base a DevMasterTeam
 */

class TaskModel {

    // mapeamento das propriedades:

    @SerializedName("Id") // referencia o mapeamento -> do "Id" para o "id" ... E assim funciona tbm p os demais abaixo
    var id: Int = 0

    // lista de prioridades
    @SerializedName("PriorityId")
    var priorityId: Int = 0

    @SerializedName("Description")
    var description: String = ""

    // data que é convertida
    @SerializedName("DueDate")
    var dueDate: String = ""

    // indica se a tarefa está completa ou não, o 'false' é só pra ter um valor padrão
    @SerializedName("Complete")
    var complete: Boolean = false

    // para a descrição da prioridade
    var priorityDescription: String = ""
}