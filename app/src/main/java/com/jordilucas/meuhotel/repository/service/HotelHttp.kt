package com.jordilucas.meuhotel.repository.service

import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.repository.Status

class HotelHttp(private val service: HotelApi, private val repository: HotelRepository,
                private val currenteUser: String) {

    fun syncronizeWithServer(){
        if(currenteUser.isBlank()){
            throw SecurityException("Usuario não encontrado")
        }else{
            sendPendingData()
            updateLocal()
        }
    }

    private fun sendPendingData(){
        val pendingHotels = repository.pending()
        pendingHotels.forEach { hotel ->
            when(hotel.status){
                Status.INSERT ->{
                    val result= service.insert(currenteUser, hotel).execute()
                    if(result.isSuccessful){
                        hotel.serverId = result.body()?.id?:0
                        hotel.status = Status.OK
                        repository.update(hotel)
                    }
                }
                Status.DELETE -> {
                    val serverId = hotel.serverId ?: 0L
                    if(serverId !=0L){
                        val result  = service.delete(currenteUser, serverId).execute()
                        if(result.isSuccessful){
                            repository.remove(hotel)
                        }
                    }else{
                        repository.remove(hotel)
                    }
                }
                Status.UPDATE -> {
                    val result = if(hotel.serverId == 0L){
                        service.insert(currenteUser, hotel).execute()
                    }else{
                        service.update(currenteUser, hotel.serverId ?: 0, hotel).execute()
                    }
                    if(result.isSuccessful){
                        hotel.serverId = result?.body()?.id ?: 0
                        hotel.status = Status.OK
                        repository.update(hotel)
                    }
                }
            }
        }
    }

    private fun updateLocal(){}

}