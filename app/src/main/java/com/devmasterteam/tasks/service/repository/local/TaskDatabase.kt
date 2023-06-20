package com.devmasterteam.tasks.service.repository.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Banco de dados
 */

// @Database(entities = [PriorityModel::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    // Singleton
    companion object {
        private lateinit var INSTANCE: TaskDatabase

        fun getDatabase(context: Context): TaskDatabase {
            // verifica se o INSTANCE não foi inicializado, se não tiver sido, será inicializado ali embaixo...
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(TaskDatabase::class) { // faz o synchronized p que só uma thread entre no código
                    // faz a instancia do banco - implementa a função com construtores que o ROOM fornece - databaseBuilder() constrói um database - "taskDB" é o nome do banco
                    INSTANCE = Room.databaseBuilder(context, TaskDatabase::class.java, "tasksDB")
                        .allowMainThreadQueries() // para que o usuário não fique esperando o processamento do app
                        .build() // inicializa o INSTANCE caso ele não tenha sido inicializado ali em cima
                }
            }
            return INSTANCE // retorna a instancia
        }
    }

}