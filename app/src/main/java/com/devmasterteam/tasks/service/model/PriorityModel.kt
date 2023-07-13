package com.devmasterteam.tasks.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Mapeia os dados que chegam da API e diz como os dados serão salvos no banco de dados
 *
 * Utiliza como base a DevMasterTeam
 */

@Entity(tableName = "Priority") // ref. o salvamento no banco de dados com o nome Priority
class PriorityModel {

    // campo Id que vem da API
    @SerializedName("Id")
    @ColumnInfo(name = "id") // salva no banco de dados o nome da coluna
    @PrimaryKey // salva no banco de dados que o id recebido pela API é único, ou seja, não será possível ids repetidos
    var id: Int = 0

    // campo Description que vem da API
    @SerializedName("Description")
    @ColumnInfo(name = "description")
    var description: String = ""

}