package com.jordilucas.meuhotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jordilucas.meuhotel.data.Hotel
import com.jordilucas.meuhotel.fragments.HotelDetailsFragment
import com.jordilucas.meuhotel.fragments.HotelListFragment

class HotelActivity : AppCompatActivity(), HotelListFragment.OnHotelClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)
    }

    override fun onHotelClick(hotel: Hotel) {
        if(isTablet()){
            showDetailsFragment(hotel.id)
        }else {
            showDetailsActivity(hotel.id)
        }
    }

    private fun isTablet() = findViewById<View>(R.id.details) != null

    private fun showDetailsFragment(hotelId:Long){
        val fragment = HotelDetailsFragment.newInstance(hotelId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            .commit()
    }

    private fun showDetailsActivity(hotelId:Long){
        HotelDetailsActivity.open(this, hotelId)
    }

}
