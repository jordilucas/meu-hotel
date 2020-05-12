package com.jordilucas.meuhotel.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import com.jordilucas.meuhotel.R
import com.jordilucas.meuhotel.details.HotelDetailsActivity
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.details.HotelDetailsFragment
import com.jordilucas.meuhotel.form.HotelFormFragment
import com.jordilucas.meuhotel.hotelList.HotelListFragment
import com.jordilucas.meuhotel.hotelList.HotelListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HotelActivity : AppCompatActivity(), HotelListFragment.OnHotelClickListener,
                    SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private var searchView:SearchView? = null

    private val viewModel: HotelListViewModel by viewModel()

    private val listFragment: HotelListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentList) as HotelListFragment
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
        if(viewModel.getSearchTerm()?.value?.isNotEmpty() == true){
            Handler().post {
                val query = viewModel.getSearchTerm()?.value
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
                AboutDialogFragment()
                    .show(supportFragmentManager, "sobre")
            R.id.action_new ->
                HotelFormFragment.newInstance().open(supportFragmentManager)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        listFragment.search(newText?:"")
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?) = true

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        listFragment.search()
        return true
    }

    override fun onHotelClick(hotel: Hotel) {
        if(isTablet()){
           viewModel.hotelIdSelected = hotel.id
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
        searchView?.setOnQueryTextListener(null)
    }

    private fun showDetailsActivity(hotelId:Long){
        HotelDetailsActivity.open(this, hotelId)
    }
}
