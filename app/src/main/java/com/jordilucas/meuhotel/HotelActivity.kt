package com.jordilucas.meuhotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jordilucas.meuhotel.data.Hotel
import com.jordilucas.meuhotel.fragments.HotelListFragment

class HotelActivity : AppCompatActivity(), HotelListFragment.OnHotelClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_hotel)
    }

    override fun onHotelClick(hotel: Hotel) {
        showDetailsHotel(hotel.id)
    }

    private fun showDetailsHotel(hotelId:Long){
        HotelDetailsActivity.open(this, hotelId)
    }

}
