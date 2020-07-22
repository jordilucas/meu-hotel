package com.jordilucas.meuhotel.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.jordilucas.meuhotel.R
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.service.HotelHttp
import kotlinx.android.synthetic.main.fragment_hotel_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotelDetailsFragment: Fragment(){
    private val viewModel: HotelDetailsViewModel by viewModel()
    private  var hotel: Hotel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hotel_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?. getLong(EXTRA_HOTEL_ID, -1)?:-1
        viewModel.loadHotelDetails(id).observe(viewLifecycleOwner, Observer { hotel ->
            if(hotel != null){
                showHotelDetailsView(hotel)
            }else{
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this)
                    ?.commit()
                errorHotelNotFound()
            }
        })
    }

    private fun showHotelDetailsView(hotel: Hotel) {
        this.hotel = hotel
        txtName.text = hotel.name
        txtAddress.text = hotel.address
        rbRatting.rating = hotel.rating
        var photoUrl = hotel.photoUrl
        if(photoUrl.isNotEmpty()){
            if(!photoUrl.contains("content://")){
                photoUrl = HotelHttp.BASE_URL + hotel.photoUrl
            }
            Glide.with(imgPhoto.context).load(photoUrl).into(imgPhoto)
        }
    }

    private fun errorHotelNotFound() {
        txtName.text = getString(R.string.error_hotel_not_found)
        txtAddress.visibility = View.GONE
        rbRatting.visibility = View.GONE
    }

    companion object{
        const val TAG_DETAILS = "tagDetails"
        private const val EXTRA_HOTEL_ID = "hotelId"

        fun newInstance(id:Long) = HotelDetailsFragment()
            .apply {
            arguments = Bundle().apply {
                putLong(EXTRA_HOTEL_ID, id)
            }
        }

    }

}