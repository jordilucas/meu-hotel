package com.jordilucas.meuhotel.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.ListFragment
import com.jordilucas.meuhotel.R
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.presenter.HotelListPresenter
import com.jordilucas.meuhotel.repository.MemoryRepository
import com.jordilucas.meuhotel.view.HotelListView

class HotelListFragment: ListFragment(), HotelListView, AdapterView.OnItemLongClickListener, ActionMode.Callback {

    private val presenter = HotelListPresenter(this, MemoryRepository)
    private var actionMode: ActionMode? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        presenter.searchHotels("")
        listView.onItemLongClickListener = this
    }

    override fun showHotels(hotels: List<Hotel>) {
        val adapter = ArrayAdapter<Hotel>(requireContext(),android.R.layout.simple_list_item_activated_1, hotels)
        listAdapter = adapter
    }

    override fun showHotelDetail(hotel: Hotel) {
        if(activity is OnHotelClickListener){
            val listener = activity as OnHotelClickListener
            listener.onHotelClick(hotel)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val hotel =l?.getItemAtPosition(position) as Hotel
        presenter.showHotelDetails(hotel)
    }

    fun search(text:String){
        presenter.searchHotels(text)
    }

    fun clearSearch(){
        presenter.searchHotels("")
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val consumed = (actionMode == null)
        if(consumed){
            val hotel = parent?.getItemAtPosition(position) as Hotel
            presenter.showDeleteMode()
            presenter.selectHotel(hotel)
        }
        return consumed
    }

    override fun showDeleteMode() {
        val appCompatActivity = (activity as AppCompatActivity)
        actionMode = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    override fun hideDeleteMode() {
        listView.onItemLongClickListener = this
        for (i in 0 until listView.count){
            listView.setItemChecked(i, false)
        }
        listView.post {
            actionMode?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }
    }

    override fun updateSelectionCountText(count: Int) {
        view?.post { actionMode?.title = resources.getQuantityString(R.plurals.list_hotel_selected, count, count) }
    }

    override fun showSelectedHotels(hotels: List<Hotel>) {
        listView.post {
            for(i in 0 until listView.count){
                val hotel = listView.getItemAtPosition(i) as Hotel
                if(hotels.find { it.id == hotel.id } != null){
                    listView.setItemChecked(i, true)
                }
            }
        }
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if(item?.itemId == R.id.action_delete){
            presenter.deleteSelected { hotels ->
                if(activity is OnHotelDeleteListener){
                    (activity as OnHotelDeleteListener).onHotelsDelete(hotels)
                }
            }
            return true
        }
        return false
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        activity?.menuInflater?.inflate(R.menu.hotel_delete_list, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        presenter.hideDeleteMode()
    }

    interface OnHotelDeleteListener{
        fun onHotelsDelete(hotels: List<Hotel>)
    }

    interface OnHotelClickListener{
        fun onHotelClick(hotel:Hotel)
    }

}