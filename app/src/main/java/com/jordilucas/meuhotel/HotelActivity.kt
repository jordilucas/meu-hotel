package com.jordilucas.meuhotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.dialogs.AboutDialogFragment
import com.jordilucas.meuhotel.fragments.HotelDetailsFragment
import com.jordilucas.meuhotel.fragments.HotelFormFragment
import com.jordilucas.meuhotel.fragments.HotelListFragment

class HotelActivity : AppCompatActivity(), HotelListFragment.OnHotelClickListener,
                    SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener,
                    HotelFormFragment.OnHotelSavedListener{

    private var lastSearchTerm:String =""
    private var searchView:SearchView? = null

    private val listFragment: HotelListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentList) as HotelListFragment
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putString(EXTRA_SEARCH_TERM, lastSearchTerm)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastSearchTerm = savedInstanceState?.getString(EXTRA_SEARCH_TERM)?:""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hotel, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)
        if(lastSearchTerm.isNotEmpty()){
            Handler().post {
                val query = lastSearchTerm
                searchItem.expandActionView()
                searchView?.setQuery(query, true)
                searchView?.clearFocus()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_info ->
                AboutDialogFragment().show(supportFragmentManager, "sobre")
            R.id.action_new ->
                HotelFormFragment.newInstance().open(supportFragmentManager)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        lastSearchTerm = newText?:""
        listFragment.search(lastSearchTerm)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?) = true

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        lastSearchTerm = ""
        listFragment.clearSearch()
        return true
    }

    override fun onHotelClick(hotel: Hotel) {
        /*if(isTablet()){
            showDetailsFragment(hotel.id)
        }else {
            showDetailsActivity(hotel.id)
        }*/
        showDetailsActivity(hotel.id)
    }

    private fun isTablet() = findViewById<View>(R.id.details) != null

    private fun showDetailsFragment(hotelId:Long){
        val fragment = HotelDetailsFragment.newInstance(hotelId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            .commit()
        searchView?.setOnQueryTextListener(null)
    }

    private fun showDetailsActivity(hotelId:Long){
        HotelDetailsActivity.open(this, hotelId)
    }

    companion object{
        const val EXTRA_SEARCH_TERM = "lastSearch"
    }

    override fun onHotelSaved(hotel: Hotel) {
        listFragment.search(lastSearchTerm)
    }

}
