package com.jordilucas.meuhotel.repository.room

import androidx.lifecycle.LiveData
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.repository.Status

class RoomRepository(database: HotelDatabase): HotelRepository {

    private val hotelDao = database.hotelDao()

    override fun save(hotel: Hotel) {
        if(hotel.id == 0L){
            hotel.status = Status.INSERT
            val id = hotelDao.insert(hotel)
            hotel.id = id
        }else{
            hotel.status = Status.UPDATE
            hotelDao.update(hotel)
        }
    }

    override fun insert(hotel: Hotel): Long {
        return hotelDao.insert(hotel)
    }

    override fun update(hotel: Hotel) {
        hotelDao.update(hotel)
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

    override fun pending(): List<Hotel> {
        return hotelDao.pending()
    }

    override fun hotelByServerId(serverId: Long): Hotel? {
        return hotelDao.hotelByServerId(serverId)
    }
}