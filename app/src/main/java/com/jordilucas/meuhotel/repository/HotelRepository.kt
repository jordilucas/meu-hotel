package com.jordilucas.meuhotel.repository

import androidx.lifecycle.LiveData
import com.jordilucas.meuhotel.model.Hotel

interface HotelRepository {
    fun save(hotel: Hotel)
    fun remove(vararg hotel: Hotel)
    fun hotelById(id:Long): LiveData<Hotel>
    fun search(term:String): LiveData<List<Hotel>>
}
