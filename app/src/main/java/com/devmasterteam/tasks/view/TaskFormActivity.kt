package com.devmasterteam.tasks.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding

    // classe que ajuda a converter lá embaixo o calendário
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy") // 'dd/MM/yyyy' padrão estabelecido para ser mostrada a data escolhida

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        // Layout
        setContentView(binding.root)
    }

    override fun onClick(v: View) {
        // botão p o DatePickerDialog
        if (v.id == R.id.button_date) {
            handleDate()
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

    // trata o DatePickerDialog - consegue mostrar a info para a interação do usuário, ao dar um ok após a escolha da data a informação vai para onDateSet p ser tratada
    private fun handleDate() {
        val calendar = Calendar.getInstance()// 'Calendar' pega a data do dispositivo - 'getInstance()' pega a instancia de um calendário
        val year = calendar.get(Calendar.YEAR) // pega o ano
        val month = calendar.get(Calendar.MONTH) // pega o mês
        val day = calendar.get(Calendar.DAY_OF_MONTH) // pega o dia
        DatePickerDialog(this, this, year, month, day).show() // fiz a implementação da interface ali em cima (solicitada pelo 'listener: this')
    }

}