package com.devmasterteam.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.tasks.databinding.FragmentAllTasksBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.TaskListener
import com.devmasterteam.tasks.view.adapter.TaskAdapter
import com.devmasterteam.tasks.viewmodel.TaskListViewModel

/**
 * Fragment que lista todas as tarefas
 *
 * Se conecta com a TaskListViewModel
 *
 * Se conecta com o Adapter
 *
 * Se conecta com o TaskListener
 *
 * RecyclerView passos: identificar o elemento | layout p a RecyclerView | Adapter
 *
 * Bundle é um conj. de infos, infos de vários tipos possíveis...
 *
 */

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel
    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!

    // instancia o Adapter
    private val adapter = TaskAdapter()

    // var dos argumentos das Fragments
    private var taskFilter = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        // passa o contexto p a 'TaskListViewModel'
        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)

        // RecyclerView:
        binding.recyclerAllTasks.layoutManager = LinearLayoutManager(context) // identifica a RecyclerView - 'layoutManager' gerencia o layout
        binding.recyclerAllTasks.adapter = adapter // recebe a instancia do Adapter

        // busca/pega os argumentos das Fragments
        taskFilter = requireArguments().getInt(TaskConstants.BUNDLE.TASKFILTER, 0) // recebe chave e valor padrão ... '0' é p o caso de uma Frag. vim sem argumento


        // faz a instancia do TaskListener - implementa os métodos de eventos de cliques das tarefas
        val listener = object : TaskListener {

            // clique na descrição de uma tarefa para p editá-la -> a tarefa é aberta mostrando todas as suas informações
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java) // passa a intenção c contexto e a Activity para qual vou navegar
                val bundle = Bundle() // intancia o Bundle, q é um conj. de infos...
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id) // coloca as infos no Bundle c chave e valor -> TaskConstants.BUNDLE.TASKID (caminho até a string/chave), id (valor)
                intent.putExtras(bundle) // passa o Bundle p a intenção
                startActivity(intent) // inicializa a Activity -> TaskFormActivity com as informações
            }

            // clique p remover uma tarefa de acordo c o seu id
            override fun onDeleteClick(id: Int) {
                viewModel.delete(id) // delete() -> TaskService -> TaskListViewModel -> TaskRepository
            }

            // clique p marcar o status de uma tarefa como completa de acordo c o seu id
            override fun onCompleteClick(id: Int) {
                viewModel.status(id, true) // status() -> TaskService -> TaskListViewModel -> TaskRepository
            }

            // clique p marcar o status de uma tarefa como incompleta de acordo c o seu id
            override fun onUndoClick(id: Int) {
                viewModel.status(id, false) // status() -> TaskService -> TaskListViewModel -> TaskRepository
            }

        }

        // passa o listener para o Adapter - p q ele deixe de ser nulo lá no Adapter e as infos sejam listadas na RecyclerView
        adapter.attachListener(listener)


        // observadores
        observe()

        return binding.root
    }


    // carrega os Fragments adequadamente após a inserção/edição de uma tarefa
    override fun onResume() {
        super.onResume()
        viewModel.list(taskFilter) // chama/recebe a lista de tarefas atualizada
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observe() {
        // observa a var 'tasks' da TaskListViewModel - 'viewLifecycleOwner' e não 'this', pois aq é uma Fragment
        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.updateTasks(it) // passa a lista de todas as tarefas p o Adapter, pois ele faz a cola do layout e os dados -> TaskAdapter
        }

        // observa a var 'delete' da TaskListViewModel - 'viewLifecycleOwner' e não 'this', pois aq é uma Fragment
        viewModel.delete.observe(viewLifecycleOwner) {
            // se não for sucesso chama a mensagem de falha
            if (!it.status()) {
                Toast.makeText(context, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        // observa a var 'status' da TaskListViewModel - 'viewLifecycleOwner' e não 'this', pois aq é uma Fragment
        viewModel.status.observe(viewLifecycleOwner) {
            // se não for sucesso chama a mensagem de falha
            if (!it.status()) {
                Toast.makeText(context, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

    }
}