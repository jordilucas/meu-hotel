package com.jordilucas.meuhotel.presenter

import com.jordilucas.meuhotel.data.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.view.HotelListView

class HotelListPresenter(
    private val view:HotelListView,
    private val repository: HotelRepository
){

    private var lastTerm=""
    private var inDeleteMode = false
    private val selectedItens = mutableListOf<Hotel>()

    fun searchHotels(term:String) {
        lastTerm = term
        repository.search(term){hotels ->
            view.showHotels(hotels)
        }
    }

    fun showHotelDetails(hotel: Hotel){
        view.showHotelDetail(hotel)
    }

    fun selectHotel(hotel:Hotel){
        if(inDeleteMode){
            toggleHotelSelected(hotel)
            if(selectedItens.size == 0){
                view.hideDeleteMode()
            }else{
                view.updateSelectionCountText(selectedItens.size)
                view.showSelectedHotels(selectedItens)
            }
        }else{
            view.showHotelDetail(hotel)
        }
    }

    private fun toggleHotelSelected(hotel: Hotel){
        val existing = selectedItens.find { it.id == hotel.id }
        if(existing == null){
            selectedItens.add(hotel)
        }else{
            selectedItens.removeAll { it.id == hotel.id }
        }
    }

    fun showDeleteMode(){
        inDeleteMode = true
        view.showDeleteMode()
    }

    fun hideDeleteMode(){
        inDeleteMode = false
        selectedItens.clear()
        view.hideDeleteMode()
    }

    fun refresh(){
        searchHotels(lastTerm)
    }

    fun deleteSelected(callback:(List<Hotel>) -> Unit){
        repository.remove(*selectedItens.toTypedArray())
        refresh()
        callback(selectedItens)
        hideDeleteMode()
    }



}