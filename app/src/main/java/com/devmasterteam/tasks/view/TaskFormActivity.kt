package com.devmasterteam.tasks.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.collection.arrayMapOf
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * 'DatePickerDialog' abre uma caixa de diálogo permitindo que o usuário selecione uma data c/ 'dd-mm-aaaa' através de um calendário...
 * É sempre necessário verificar na documentação da API qual o formato de data que ela aceita...
 *
 * 'Spinner' é um campo para digitação de valores (geralmente números ou datas) que possui dois botões de controle, os quais permitem aumentar ou reduzir o valor do input,
 * mas tbm serve p a escolha de alguma coisa dentre várias outras, como no caso das prioridades...
 *
 * o 'Adapter' faz a cola entre os elementos e o layout...
 *
 */

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding

    // classe que ajuda a converter lá embaixo o calendário
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy") // 'dd/MM/yyyy' padrão estabelecido para ser mostrada a data escolhida

    // intancia uma lista vazia p expor/dar acesso à lista de prioridades
    private var listPriority: List<PriorityModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)


        // chama as prioridades do Spinner - 'loadPriorities' é a fun criada na TaskFormViewModel
        viewModel.loadPriorities()

        // chama a priorityList da TaskFormViewModel
        observe()


        // layout
        setContentView(binding.root)
    }

    override fun onClick(v: View) {
        // botão p o DatePickerDialog
        if (v.id == R.id.button_date) {
            handleDate() // chama a função
            // botão p adicionar tarefa
        } else if (v.id == R.id.button_save) {
            handleSave() // chama a função
        }
    }

    // trata a informação recebida da interação do usuário no DatePickerDialog, ou seja, a data escolhida precisa ser mostrada na tela
    override fun onDateSet(v: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        // transforma as infos do ano, mês e dia em uma string e coloca na tela(botão)
        val calendar = Calendar.getInstance() // aqui o calendário nasce com a data do dia
        calendar.set(year, month, dayOfMonth) // e aqui a data é atualizada para a data escolhida pelo usuário

        // converte o calendário com a ajuda da classe SimpleDateFormat - pega o tempo específico do calendário p ser formatado de acordo com o padrão estabelecido lá em cima
        val dueDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = dueDate

    }

     // trata o Spinner (código que preenche o Spinner, traz as prioridades salvas no banco de dados), ao clicar na setinha todas as prioridades são mostradas ao usuário
    private fun observe() {
         // observa a priorityList da TaskFormViewModel
        viewModel.priorityList.observe(this) { // traz a lista de prioridades - 'it' é a lista
            listPriority = it
            val list = mutableListOf<String>() // lista de prioridades que aparecem ao clicar na setinha da 'Prioridade'
            // passa item por item da lista...
            for (p in it) { // p cada 'p' que está dentro de it (lista) ...
                list.add(p.description) // add a descrição ...
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list) // coloca no ArrayAdapter as descrições
            binding.spinnerPriority.adapter = adapter
        }

         // observa a taskSave da TaskFormViewModel
         viewModel.taskSave.observe(this) { // 'it' é a ValidationModel
             if (it.status()) { // se o status do cadastro da tarefa for de sucesso, mostra a mensagem de sucesso e fecha a Activity
                toast("Tarefa adicionada com sucesso") // 'toast' é a fun p as mensagens de sucesso e falha
                 finish()
             } else { // caso contrário, mostra a mensagem de falha
                 toast(it.message()) // 'toast' é a fun p as mensagens de sucesso e falha
             }
         }
    }

    // para as mensagens de sucesso e falha - p simplificar o código
    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    // trata o botão de add tarefa
    private fun handleSave() {
        // instancia a TaskModel p agrupar os campos preenchidos p criar/add as tarefas
        val task = TaskModel().apply {
            this.id = 0 // por ser uma tarefa nova não existirá um 'id' por ora, por isso foi colocado 0
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()

            // 'selectedItemPosition' é o index da posição do spinner, pega a posição q o spinner está selecionado - ele guia p a obtenção do id correto da prioridade
            val index = binding.spinnerPriority.selectedItemPosition // index q está selecionado
            this.priorityId = listPriority[index].id // obtém o id da prioridade de acordo com o index da lista de prioridades
        }

        viewModel.save(task) // faz a chamada, fala p a viewModel salvar o agrupamento dos campos preenchidos p criar/add as tarefas - fun 'save' na TaskFormViewModel
    }

    // trata o DatePickerDialog - consegue mostrar a info para a interação do usuário, ao dar um ok após a escolha da data a informação vai para onDateSet p ser tratada
    private fun handleDate() {
        val calendar = Calendar.getInstance()// 'Calendar' pega a data do dispositivo - 'getInstance()' pega a instancia de um calendário
        val year = calendar.get(Calendar.YEAR) // pega o ano
        val month = calendar.get(Calendar.MONTH) // pega o mês
        val day = calendar.get(Calendar.DAY_OF_MONTH) // pega o dia
        DatePickerDialog(this, this, year, month, day).show() // fiz a implementação da interface ali em cima (solicitada pelo 'listener: this')
    }

}