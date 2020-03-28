package com.jordilucas.meuhotel.view

import com.jordilucas.meuhotel.data.Hotel

interface HotelDetailsView {
    fun showHotelDetailsView(hotel: Hotel)
    fun errorHotelNotFound()
}