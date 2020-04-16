package com.jordilucas.meuhotel.repository.room

import androidx.lifecycle.LiveData
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository

class RoomRepository(database: HotelDatabase): HotelRepository {

    private val hotelDao = database.hotelDao()

    override fun save(hotel: Hotel) {
        if(hotel.id == 0L){
            val id = hotelDao.insert(hotel)
            hotel.id = id
        }else{
            hotelDao.update(hotel)
        }
    }

    override fun remove(vararg hotel: Hotel) {
        hotelDao.delete(*hotel)
    }

    override fun hotelById(id: Long): LiveData<Hotel> {
        return hotelDao.hotelById(id)
    }

    override fun search(term: String): LiveData<List<Hotel>> {
        return hotelDao.search(term)
    }
}