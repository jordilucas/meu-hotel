package com.jordilucas.meuhotel.fragments

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.ListFragment
import com.jordilucas.meuhotel.data.Hotel
import com.jordilucas.meuhotel.presenter.HotelListPresenter
import com.jordilucas.meuhotel.repository.MemoryRepository
import com.jordilucas.meuhotel.view.HotelListView

class HotelListFragment: ListFragment(), HotelListView {

    private val presenter = HotelListPresenter(this, MemoryRepository)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.searchHotels("")
    }

    override fun showHotels(hotels: List<Hotel>) {
        val adapter = ArrayAdapter<Hotel>(requireContext(),android.R.layout.simple_list_item_1, hotels)
        listAdapter = adapter
    }

    override fun showHotelDetail(hotel: Hotel) {
        TODO("Not yet implemented")
    }
}