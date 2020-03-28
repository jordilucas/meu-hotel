package com.jordilucas.meuhotel.view

import com.jordilucas.meuhotel.data.Hotel

interface HotelListView {
    fun showHotels(hotels:List<Hotel>)
    fun showHotelDetail(hotel:Hotel)
}