package com.jordilucas.meuhotel.model

import androidx.room.Entity
import com.jordilucas.meuhotel.repository.sqlite.TABLE_HOTEL

@Entity(tableName = TABLE_HOTEL)
data class Hotel(
    var id:Long=0,
    var name:String="",
    var address:String="",
    var rating:Float=0.0F
){
    override fun toString(): String = name
}