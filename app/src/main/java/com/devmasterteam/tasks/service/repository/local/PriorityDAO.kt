package com.devmasterteam.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devmasterteam.tasks.service.model.PriorityModel

/**
 * Métodos que acessam o banco de dados... como o SELECT, INSERT, UPTADE, DELETE
 *
 * Se conecta com PriorityRepository
 *
 * 'Query' é uma consulta executada no banco de dados e dentro eu coloco a Query a ser executada
 */

@Dao
interface PriorityDAO {

    // insere no banco de dados ... pega a lista de prioridade e insere de acordo com o mapeamento feito na PriorityModel
    @Insert
    fun save(list: List<PriorityModel>)

    // seleciona todas as colunas da tabela de prioridades
    @Query("SELECT * FROM Priority")
    fun list(): List<PriorityModel>

    // retorna a descrição da prioridade onde o id for igual a :id - o Room faz a interpolação e troca o valor
    @Query("SELECT description FROM Priority WHERE id = :id")
    fun getDescription(id: Int): String

    // deleta as coisas da lista de prioridades
    @Query("DELETE FROM Priority")
    fun clear()

}