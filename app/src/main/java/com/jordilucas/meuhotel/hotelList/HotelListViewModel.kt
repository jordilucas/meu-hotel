package com.jordilucas.meuhotel.hotelList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jordilucas.meuhotel.common.SingleLiveEvent
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.repository.Status

class HotelListViewModel(private val repository: HotelRepository): ViewModel() {

    var hotelIdSelected:Long = -1
    private val searchTerm = MutableLiveData<String>()
    private val hotels = Transformations.switchMap(searchTerm){ term -> repository.search("%$term%")}
    private val inDeletedMode = MutableLiveData<Boolean>().apply { value = false }
    private val selectedItems = mutableListOf<Hotel>()
    private val selectionCount = MutableLiveData<Int>()
    private val selectedHotels = MutableLiveData<List<Hotel>>().apply { value = selectedItems }
    private val deletedItems= mutableListOf<Hotel>()
    private val showDeletedMessage = SingleLiveEvent<Int>()
    private val showDetailsCommand = SingleLiveEvent<Hotel>()
    fun isInDeleteMode():LiveData<Boolean> = inDeletedMode

    fun getSearchTerm():LiveData<String>? = searchTerm
    fun getHotels():LiveData<List<Hotel>>? = hotels
    fun selectionCount():LiveData<Int> = selectionCount
    fun selectedHotels():LiveData<List<Hotel>> = selectedHotels
    fun showDeletedMessage():LiveData<Int> = showDeletedMessage
    fun showDetailsCommand():LiveData<Hotel> = showDetailsCommand

    fun selectHotel(hotel:Hotel){
        if(inDeletedMode.value == true){
            toggleHotelSelected(hotel)
            if(selectedItems.size == 0){
                inDeletedMode.value = false
            }else{
                selectionCount.value = selectedItems.size
                selectedHotels.value = selectedItems
            }
        }else{
            showDetailsCommand.value = hotel
        }
    }

    private fun toggleHotelSelected(hotel:Hotel){
        val existing = selectedItems.find { it.id == hotel.id }
        if(existing == null){
            selectedItems.add(hotel)
        }else{
            selectedItems.removeAll { it.id == hotel.id }
        }
    }

    fun search(term:String=""){
        searchTerm.value =term
    }

    fun setInDeleteMode(deleteMode:Boolean){
        if(!deleteMode){
            selectionCount.value = 0
            selectedItems.clear()
            selectedHotels.value = selectedItems
            showDeletedMessage.value = selectedItems.size
        }
        inDeletedMode.value = deleteMode
    }

    fun deleteSelected(){
        selectedItems.forEach {
            it.status = Status.DELETE
            repository.update(it)
        }
        deletedItems.clear()
        deletedItems.addAll(selectedItems)
        setInDeleteMode(false)
        showDeletedMessage.value = deletedItems.size
    }

    fun undoDelete(){
        if(deletedItems.isNotEmpty()){
            for(hotel in deletedItems){
                hotel.id = 0L
                repository.save(hotel)
            }
        }
    }

}