package com.jordilucas.meuhotel.view

import com.jordilucas.meuhotel.data.Hotel

interface HotelFormView {
    fun showHotel(hotel: Hotel)
    fun errorInvalidHotel()
    fun errorSaveHotel()
}