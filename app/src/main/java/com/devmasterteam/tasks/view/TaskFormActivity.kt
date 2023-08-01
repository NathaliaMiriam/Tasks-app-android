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
import com.devmasterteam.tasks.service.constants.TaskConstants
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

    // id da tarefa - 0 é o valor padrão de taskIdentification (numa criação o id é sempre 0, na edição o id é sempre diferente de 0)
    private var taskIdentification = 0


    // fun chamada com a inicialização da Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)


        // chama/carrega da 'TaskFormViewModel' as prioridades do Spinner
        viewModel.loadPriorities()

        // chama/carrega as infos que constam dentro de uma tarefa
        loadDataFromActivity()

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


    // carrega as infos que constam dentro de uma tarefa
    private fun loadDataFromActivity() {
        val bundle = intent.extras // pega as infos extras usando a própria var da Activity -> var intent -> na AllTasksFragment
        // se o Bundle for diferente de nulo, as infos são buscadas
        if (bundle != null) {
            taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID) // instancia a 'taskIdentification' c as infos do Bundle (id da tarefa)
            viewModel.load(taskIdentification)// busca/recebe da 'TaskFormViewModel' a tarefa com seu id
        }
    }


    // trata/encontra a posição(index) da prioridade(priorityId) dentro do spinner
    private fun getIndex(priorityId: Int): Int {
        var index = 0 // o index começa na posição 0
        // percorre a lista buscando a posição do 'priorityId'
        for (l in listPriority) { // p cada elemento na lista de prioridades... recebe o tratamento de 'l'
            if (l.id == priorityId) {
                break // quebra o laço de repetição e retorna o index
            }
            index++ // continua incrementando até encontrar o index
        }
        return index
    }


    private fun observe() {

         // trata o Spinner (código que preenche o Spinner, traz as prioridades salvas no banco de dados), ao clicar na setinha todas as prioridades são mostradas ao usuário
         // observa a var priorityList da TaskFormViewModel
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

         // observa a var taskSave da TaskFormViewModel
         viewModel.taskSave.observe(this) { // 'it' é a ValidationModel
             if (it.status()) { // se o status do cadastro da tarefa for de sucesso... faz a verificação se é uma criação ou edição
                 if (taskIdentification == 0) { // se o id da tarefa for 0, é uma criação e mostra a mensagem
                     toast("Tarefa adicionada com sucesso.")
                 } else { // caso contrário, é uma edição e mostra a mensagem
                     toast("Tarefa editada com sucesso.")
                 }
                 finish() // e fecha a Activity
             } else { // caso contrário, se o status do cadastro da tarefa for de insucesso... mostra a mensagem de falha
                 toast(it.message())
             }
         }

        // observa a var task da TaskFormViewModel p saber se a tarefa SIM carregou ... (se for carregada, as suas infos serão mostradas ao usuário) ...
        viewModel.task.observe(this) {
            // as infos são atribuídas ao xml da Activity:
            binding.editDescription.setText(it.description) // passa a info da descrição
            binding.spinnerPriority.setSelection(getIndex(it.priorityId)) // passa a info da prioridade de acordo com o seu index
            binding.checkComplete.isChecked = it.complete // passa a info de tarefa completa ou incompleta ('true' -> completa | 'false' -> incompleta)

            // passa a info da data -> pega o valor que vem da API -> converte p uma data -> formata p o jeito que quero
            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate) // converte a data p o tipo q quero
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)  // formata a data
        }

        // observa a var taskLoad da TaskFormViewModel p saber se a tarefa NÃO carregou ... (se não for carregada, será mostrada a mensagem de erro ao usuário) ...
        viewModel.taskLoad.observe(this) {
            if (!it.status()) { // se a tarefa não for carregada...
                toast(it.message()) // é mostrada a mensagem de erro ...
                finish() // e a Activity é fechada
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
        val task = TaskModel().apply { // aplica os valores q vem da interface:
            this.id = taskIdentification
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