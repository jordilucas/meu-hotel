package com.jordilucas.meuhotel.view

import com.jordilucas.meuhotel.model.Hotel

interface HotelFormView {
    fun showHotel(hotel: Hotel)
    fun errorInvalidHotel()
    fun errorSaveHotel()
}