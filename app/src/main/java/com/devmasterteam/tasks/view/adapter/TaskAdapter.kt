package com.devmasterteam.tasks.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.tasks.databinding.RowTaskListBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.view.viewholder.TaskViewHolder

/**
 * Adapter - faz a cola entre o layout e os dados
 *
 * Se conecta com a AllTasksFragment (que lista todas as tarefas)
 *
 * Se conecta com a TaskViewHolder
 *
 */

class TaskAdapter : RecyclerView.Adapter<TaskViewHolder>() {

    private var listTasks: List<TaskModel> = arrayListOf() // lista de tarefas começa vazia

    // instância do listener p q ele deixe de ser nulo e as infos sejam listadas na RecyclerView
    private var listener: TaskListener = object : TaskListener {
        override fun onListClick(id: Int) {
            TODO("Not yet implemented")
        }

        override fun onDeleteClick(id: Int) {
            TODO("Not yet implemented")
        }

        override fun onCompleteClick(id: Int) {
            TODO("Not yet implemented")
        }

        override fun onUndoClick(id: Int) {
            TODO("Not yet implemented")
        }

    }

    // cria o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowTaskListBinding.inflate(inflater, parent, false) // pega a lista
        return TaskViewHolder(itemBinding, listener)
    }

    // popula cada viewholder com as informações necessárias
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(listTasks[position]) // pega o layout criado, pega a info da tarefa e junta os dois --> TaskViewHolder
    }

    // tamanho da lista, pra RecyclerView saber como ela se organiza, pra saber qts linhas vai criar
    override fun getItemCount(): Int {
        return listTasks.count()
    }

    // atualiza a lista de tarefas do Adapter
    fun updateTasks(list: List<TaskModel>) {
        listTasks = list
        notifyDataSetChanged() // avisa o Adapter q a info foi alterada e q ele precisa se atualizar
    }

    fun attachListener(taskListener: TaskListener) {
        listener = taskListener
    }

}