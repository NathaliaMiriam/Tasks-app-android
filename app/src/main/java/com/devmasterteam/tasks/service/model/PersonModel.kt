package com.devmasterteam.tasks.service.model

import com.google.gson.annotations.SerializedName

/**
 * Mapeia as infos retornadas, as infos que chegam do usuário, quando o mesmo faz login ou criação de uma conta
 *
 * Faz conexão com as informações do usuário -> PersonService
 *
 * Faz a conversão de JSON p essa classe
 *
 * Faz conexão com o repositório -> PersonRepository
*/

class PersonModel {

    @SerializedName("token") // info que está chegando do JSON
    lateinit var token: String

    @SerializedName("personKey") // info que está chegando do JSON
    lateinit var personKey: String

    @SerializedName("name") // info que está chegando do JSON
    lateinit var name: String
}