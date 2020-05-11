package com.jordilucas.meuhotel.di

import com.jordilucas.meuhotel.details.HotelDetailsViewModel
import com.jordilucas.meuhotel.form.HotelFormViewModel
import com.jordilucas.meuhotel.hotelList.HotelListViewModel
import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.repository.room.HotelDatabase
import com.jordilucas.meuhotel.repository.room.RoomRepository
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val androidModule = module{
    single{this}
    single{
        RoomRepository(HotelDatabase.getDatabase(context = get())) as
                HotelRepository
    }
    viewModel{
        HotelListViewModel(repository = get())
    }
    viewModel {
        HotelDetailsViewModel(repository = get())
    }
    viewModel {
        HotelFormViewModel(repository = get())
    }
}