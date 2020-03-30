package com.jordilucas.meuhotel.view

import com.jordilucas.meuhotel.model.Hotel

interface HotelListView {
    fun showHotels(hotels:List<Hotel>)
    fun showHotelDetail(hotel:Hotel)
    fun showDeleteMode()
    fun hideDeleteMode()
    fun showSelectedHotels(hotels:List<Hotel>)
    fun updateSelectionCountText(count:Int)
}