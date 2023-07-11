package com.devmasterteam.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devmasterteam.tasks.service.model.PriorityModel

/**
 * Métodos que acessam o banco de dados... como o SELECT, INSERT, UPTADE, DELETE
 *
 * 'Query' é uma consulta executada no banco de dados e dentro eu coloco a Query a ser executada
 */

@Dao
interface PriorityDAO {

    @Insert // insere no banco de dados ... pega a lista de prioridade e insere de acordo com o mapeamento feito na PriorityModel
    fun save(list: List<PriorityModel>)

    @Query("SELECT * FROM Priority") // seleciona todas as colunas da tabela de prioridades
    fun list(): List<PriorityModel>

    // deleta as coisas da lista de prioridades
    @Query("DELETE FROM Priority")
    fun clear()

}