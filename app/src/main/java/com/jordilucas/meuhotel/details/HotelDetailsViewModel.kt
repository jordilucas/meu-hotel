package com.jordilucas.meuhotel.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository

class HotelDetailsViewModel(private val repository: HotelRepository): ViewModel() {

    fun loadHotelDetails(id:Long): LiveData<Hotel>{
        return repository.hotelById(id)
    }

}