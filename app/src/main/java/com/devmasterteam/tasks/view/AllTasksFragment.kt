package com.devmasterteam.tasks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.tasks.databinding.FragmentAllTasksBinding
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.view.adapter.TaskAdapter
import com.devmasterteam.tasks.viewmodel.TaskListViewModel

/**
 * Fragment que lista todas as tarefas
 *
 * RecyclerView passos: identificar o elemento | layout p a RecyclerView | Adapter
 *
 * Se conecta com a TaskListener (eventos possíveis para cada tarefa)
 */

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    // instancia o Adapter
    private val adapter = TaskAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        // passa o contexto p a 'TaskListViewModel'
        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        // RecyclerView:
        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context) // identifica a RecyclerView - 'layoutManager' gerencia o layout
        binding.recyclerAllTasks.adapter = adapter // recebe a instancia do Adapter

        // instância do listener p q ele deixe de ser nulo lá no Adapter e as infos sejam listadas na RecyclerView
        val listener = object : TaskListener {
            override fun onListClick(id: Int) {

            }

            override fun onDeleteClick(id: Int) {

            }

            override fun onCompleteClick(id: Int) {

            }

            override fun onUndoClick(id: Int) {

            }

        }

        // passa o listener para o Adapter
        adapter.attachListener(listener)


        // observadores
        observe()

        return binding.root
    }

    // carrega o Fragment adequadamente após a inserção/edição de uma tarefa
    override fun onResume() {
        super.onResume()
        viewModel.list() // chama a lista de tarefas atualizada
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        // observa a var 'tasks' da TaskListViewModel - 'viewLifecycleOwner' e não 'this', pois aq é uma fragment
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.updateTasks(it) // passa a lista de todas as tarefas p o Adapter, pois ele faz a cola do layout e os dados -> TaskAdapter
        }

    }
}