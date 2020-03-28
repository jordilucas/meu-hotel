package com.jordilucas.meuhotel.presenter

import com.jordilucas.meuhotel.data.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.view.HotelListView

class HotelListPresenter(
    private val view:HotelListView,
    private val repository: HotelRepository
){
    fun searchHotels(term:String) {
        repository.search(term){hotels ->
            view.showHotels(hotels)
        }
    }

    fun showHotelDetails(hotel: Hotel){
        view.showHotelDetail(hotel)
    }
}