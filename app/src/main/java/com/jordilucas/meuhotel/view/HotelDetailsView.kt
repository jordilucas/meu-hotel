package com.jordilucas.meuhotel.view

import com.jordilucas.meuhotel.model.Hotel

interface HotelDetailsView {
    fun showHotelDetailsView(hotel: Hotel)
    fun errorHotelNotFound()
}