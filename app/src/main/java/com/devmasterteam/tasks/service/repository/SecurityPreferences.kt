package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences - acesso a dados rápidos do projeto, não tão complexos, que não sofrem mudanças frequentes
 *                   - salva os dados como se fosse um banco de dados, ou seja, toda e qualquer info add na aplicação pelo usuário fica salva
 *
 * Recebe e salva os dados de login do usuário (e-mail | senha)
 */

class SecurityPreferences(context: Context) { // para eu conseguir instanciar lá na LoginViewModel é necessário o contexto aqui...

    private val preferences: SharedPreferences =
        context.getSharedPreferences("taskShared", Context.MODE_PRIVATE)

    // salva dados
    fun store(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    // pega dados
    fun get(key: String): String {
        return preferences.getString(key, "") ?: "" // se não encontrar uma chave, retorna uma string vazia, se for nulo ou algo do tipo, retorna uma string vazia
    }

}