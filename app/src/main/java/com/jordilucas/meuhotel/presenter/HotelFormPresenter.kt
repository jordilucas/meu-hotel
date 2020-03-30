package com.jordilucas.meuhotel.presenter

import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.repository.HotelValidator
import com.jordilucas.meuhotel.view.HotelFormView

class HotelFormPresenter(private val view:HotelFormView, private val repository: HotelRepository) {

    private val validator = HotelValidator()

    fun loadHotel(id:Long){
        repository.hotelById(id){ hotel ->
            if(hotel!=null){
                view.showHotel(hotel)
            }
        }
    }

    fun saveHotel(hotel:Hotel):Boolean{
        return if(validator.validate(hotel)){
            try {
                repository.save(hotel)
                true
            }catch (e:Exception){
                view.errorSaveHotel()
                false
            }
        }
        else{
            view.errorInvalidHotel()
            false
        }
    }

}