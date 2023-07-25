package com.devmasterteam.tasks.view.viewholder

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.RowTaskListBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.TaskModel

/**
 * ViewHolder - wrapper (embrulho) em torno da View que contém o layout de um item individual na lista
 *
 * Se conecta com o Adapter (TaskAdapter)
 */

class TaskViewHolder(private val itemBinding: RowTaskListBinding, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(task: TaskModel) {

        itemBinding.textDescription.text = task.description // descrição da tarefa
        itemBinding.textPriority.text = task.priorityId.toString()  // prioridade da tarefa
        itemBinding.textDueDate.text = task.dueDate // data limite da tarefa

        // Eventos
        // itemBinding.textDescription.setOnClickListener { listener.onListClick(task.id) } // clique na descrição
        // itemBinding.imageTask.setOnClickListener { } // clique na imagem

        // 'setOnLongClickListener' clique longo na descrição
        itemBinding.textDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { dialog, which ->
                    // listener.onDeleteClick(task.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()
            true
        }

    }
}