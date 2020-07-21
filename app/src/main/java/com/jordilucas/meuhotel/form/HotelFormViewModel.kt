package com.jordilucas.meuhotel.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository

class HotelFormViewModel(private val repository: HotelRepository):ViewModel() {

    private val validator by lazy { HotelValidator() }
    val photoUrl = MutableLiveData<String>()

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