package com.jordilucas.meuhotel.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.jordilucas.meuhotel.details.HotelDetailsViewModel
import com.jordilucas.meuhotel.form.HotelFormViewModel
import com.jordilucas.meuhotel.hotelList.HotelListViewModel
import com.jordilucas.meuhotel.repository.HotelRepository
import com.jordilucas.meuhotel.repository.imagefiles.FindHotelPicture
import com.jordilucas.meuhotel.repository.imagefiles.ImageGalleryPictureFinder
import com.jordilucas.meuhotel.repository.room.HotelDatabase
import com.jordilucas.meuhotel.repository.room.RoomRepository
import com.jordilucas.meuhotel.repository.service.HotelHttp
import com.jordilucas.meuhotel.repository.service.HotelHttpApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val androidModule = module {
    single { this }
    single {
        RoomRepository(HotelDatabase.getDatabase(context = get())) as HotelRepository
    }
    viewModel {
        HotelListViewModel(repository = get())
    }
    viewModel {
        HotelDetailsViewModel(repository = get())
    }
    viewModel {
        HotelFormViewModel(repository = get())
    }
    factory {
        val context = get() as Context
        val resolver = context.contentResolver
        val uploadDir = context.externalCacheDir ?: context.cacheDir
        ImageGalleryPictureFinder(uploadDir, resolver) as FindHotelPicture
    }
    single {

        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
       // val httpClient = OkHttpClient.Builder()
        //httpClient.addInterceptor(logging)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(HotelHttp.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
        retrofit.create<HotelHttpApi>(HotelHttpApi::class.java)

    }



    factory {
        HotelHttp(
            service = get(),
            repository = get(),
            pictureFinder = get(),
            currentUser = "jordi"
        )
    }

}