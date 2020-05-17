package com.jordilucas.meuhotel.repository

import androidx.lifecycle.LiveData
import com.jordilucas.meuhotel.model.Hotel

interface HotelRepository {
    fun save(hotel: Hotel)
    fun insert(hotel: Hotel) : Long
    fun update(hotel: Hotel)
    fun remove(vararg hotel: Hotel)
    fun hotelById(id:Long): LiveData<Hotel>
    fun search(term:String): LiveData<List<Hotel>>
    fun pending():List<Hotel>
    fun hotelByServerId(serverId: Long): Hotel?
}
