package com.devmasterteam.tasks.view.viewholder

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.RowTaskListBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.PriorityRepository
import java.text.SimpleDateFormat

/**
 * ViewHolder - wrapper (embrulho) em torno da View que contém o layout de um item individual na lista
 *
 * Se conecta com o Adapter -> TaskAdapter
 *
 * Se conecta com a TaskListener (eventos possíveis para cada tarefa)
 */

class TaskViewHolder(private val itemBinding: RowTaskListBinding, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemBinding.root) {


    // preenche as linhas da RecyclerView, cada linha é uma tarefa
    fun bindData(task: TaskModel) {

        itemBinding.textDescription.text = task.description // descrição da tarefa

        itemBinding.textPriority.text = task.priorityDescription // descrição da prioridade vêm do modelo - 'priorityDescription' é a fun -> TaskModel

        // data limite da tarefa - 'SimpleDateFormat()' formata a data
        val date = SimpleDateFormat("yyyy-MM-dd").parse(task.dueDate) // converte a data que vêm inicialmente p o objeto 'date'
        itemBinding.textDueDate.text = SimpleDateFormat("dd/MM/yyyy").format(date) // formata o objeto 'date' p o formato de data que quero

        // altera a imagem (círculo c/ check ou círculo vazio) de acordo com o status da tarefa: tarefa feita ou tarefa não feita
        if (task.complete) { // se a tarefa estiver completa ...
            itemBinding.imageTask.setImageResource(R.drawable.ic_done) // adiciona a imagem do círculo c/ check
        } else { // caso contrário ...
            itemBinding.imageTask.setImageResource(R.drawable.ic_todo) // adiciona a imagem do círculo vazio
        }

        // eventos de cliques p cada tarefa -> TaskListener:

        itemBinding.textDescription.setOnClickListener { listener.onListClick(task.id) } // clique na descrição da tarefa p editá-la

        itemBinding.imageTask.setOnClickListener { // clique na imagem (círculo c/ check ou círculo vazio) p completar ou descompletar a tarefa
            if (task.complete) { // se a tarefa estiver completa ..
                listener.onUndoClick(task.id) // descompleta a tarefa
            } else { // caso contrário ...
                listener.onCompleteClick(task.id) // completa a tarefa
            }
        }

        itemBinding.textDescription.setOnLongClickListener { // 'setOnLongClickListener' clique longo na descrição p remover a tarefa
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { dialog, which -> // ao clicar no sim ...
                    listener.onDeleteClick(task.id) // remove a tarefa
                }
                .setNeutralButton(R.string.cancelar, null) // ao clicar em cancelar, nada é feito ...
                .show() // mostra a tarefa
            true
        }

    }
}