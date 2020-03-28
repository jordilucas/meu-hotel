package com.jordilucas.meuhotel.repository

import com.jordilucas.meuhotel.data.Hotel

interface HotelRepository {
    fun save(hotel: Hotel)
    fun remove(vararg hotel: Hotel)
    fun hotelById(id:Long, callback:(Hotel?)->Unit)
    fun search(term:String, callback: (List<Hotel>) -> Unit)
}

object MemoryRepository:HotelRepository{
    private var nextId = 1L
    private val hotelList = mutableListOf<Hotel>()
    init{
        save(Hotel(0,"Aquarela Palace Hotel","Rua Joao Pinto1", 4.5F))
        save(Hotel(0,"Jp Hotel","Rua Joao Pinto2", 4.5F))
        save(Hotel(0,"Hotel Infinito","Rua Joao Pinto3", 4.5F))
        save(Hotel(0,"Hotel Cool","Rua Joao Pinto4", 4.5F))
        save(Hotel(0,"Hotel Tio Joao","Rua Joao Pinto5", 4.5F))
        save(Hotel(0,"Hotel Casa Grande","Rua Joao Pinto6", 4.5F))
        save(Hotel(0,"Hotel4","Rua Joao Pinto7", 4.5F))
        save(Hotel(0,"Hotel123","Rua Joao Pinto8", 4.5F))
        save(Hotel(0,"Hotel 000","Rua Joao Pinto9", 4.5F))
    }

    override fun save(hotel: Hotel) {
        if (hotel.id==0L){
            hotel.id = nextId++
            hotelList.add(hotel)
        }else{
            val index = hotelList.indexOfFirst {  it.id == hotel.id }
            if(index > -1){
                hotelList[index] = hotel
            }else{
                hotelList.add(hotel)
            }
        }
    }

    override fun remove(vararg hotel: Hotel) {
        hotelList.removeAll(hotel)
    }

    override fun hotelById(id: Long, callback: (Hotel?) -> Unit) {
        callback(hotelList.find { it.id == id })
    }

    override fun search(term: String, callback: (List<Hotel>) -> Unit) {
        callback(
            if (term.isEmpty()) hotelList
                else hotelList.filter {
                it.name.toUpperCase().contains(term.toUpperCase())
            }
        )
    }

}