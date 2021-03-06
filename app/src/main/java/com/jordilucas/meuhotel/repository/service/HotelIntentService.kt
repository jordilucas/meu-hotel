package com.jordilucas.meuhotel.repository.service

import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.android.inject

class HotelIntentService: JobIntentService() {
    override fun onHandleWork(intent: Intent) {
        val hotelHttp: HotelHttp by inject()
        val it = Intent(ACTION_SYNC)
        val lbm = LocalBroadcastManager.getInstance(this)
        try {
            hotelHttp.syncronizeWithServer()
            it.putExtra(EXTRA_SUCCESS, true)
        }catch (e:Exception){
            it.putExtra(EXTRA_SUCCESS, false)
            e.printStackTrace()
        }finally {
            lbm.sendBroadcast(it)
        }
    }

    companion object{
        const val ACTION_SYNC = "sync_hotels"
        const val EXTRA_SUCCESS = "success"
    }

}