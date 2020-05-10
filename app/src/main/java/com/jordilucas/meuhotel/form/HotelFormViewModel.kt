package com.jordilucas.meuhotel.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.repository.HotelValidator

class HotelFormViewModel(private val repository: HotelRepository):ViewModel() {

    private val validator by lazy { HotelValidator() }

    fun loadHotel(id:Long):LiveData<Hotel>{
        return repository.hotelById(id)
    }

    fun saveHotel(hotel:Hotel):Boolean{
        return validator.validate(hotel)
            .also { validated ->
                if(validated)repository.save(hotel)
            }
    }

}