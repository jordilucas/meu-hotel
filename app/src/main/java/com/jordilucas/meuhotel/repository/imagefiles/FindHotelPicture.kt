package com.jordilucas.meuhotel.repository.imagefiles

import com.jordilucas.meuhotel.model.Hotel

interface FindHotelPicture {
    fun pictureFile(hotel:Hotel):PictureToUpload
}