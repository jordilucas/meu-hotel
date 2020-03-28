package com.jordilucas.meuhotel.presenter

import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.view.HotelDetailsView

class HotelDetailsPresenter(
    private val view:HotelDetailsView,
    private val repository: HotelRepository
) {

    fun loadHotelDetails(id:Long){
        repository.hotelById(id){ hotel ->
            if(hotel != null){
                view.showHotelDetailsView(hotel)
            }else{
                view.errorHotelNotFound()
            }
        }
    }

}