package com.jordilucas.meuhotel.hotelList

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.app.JobIntentService
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.jordilucas.meuhotel.R
import com.jordilucas.meuhotel.model.Hotel
import com.jordilucas.meuhotel.repository.service.HotelIntentService
import kotlinx.android.synthetic.main.fragment_list_hotel.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HotelListFragment : ListFragment(), AdapterView.OnItemLongClickListener,
    ActionMode.Callback, SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: HotelListViewModel by sharedViewModel()
    private var actionMode: ActionMode? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.onItemLongClickListener = this

        viewModel.showDetailsCommand().observe(viewLifecycleOwner, Observer { hotel ->
            if (hotel != null) {
                showHotelDetail(hotel)
            }
        })
        viewModel.isInDeleteMode().observe(viewLifecycleOwner, Observer { deleteMode ->
            if (deleteMode == true) {
                showDeleteMode()
            } else {
                hideDeleteMode()
            }
        })
        viewModel.selectedHotels().observe(viewLifecycleOwner, Observer { hotels ->
            if (hotels != null) {
                showSelectedHotels(hotels)
            }
        })
        viewModel.selectionCount().observe(viewLifecycleOwner, Observer { count ->
            if (count != null) {
                updateSelectionCountText(count)
            }
        })
        viewModel.showDeletedMessage().observe(viewLifecycleOwner, Observer { count ->
            if (count != null && count > 0) {
                showMessageHotelsDeleted(count)
            }
        })
        viewModel.getHotels()?.observe(viewLifecycleOwner, Observer { hotels ->
            if (hotels != null) {
                showHotels(hotels)
            }
        })
        if (viewModel.getHotels()?.value == null) {
            search()
        }
    }

    private fun showHotels(hotels: List<Hotel>) {
        val adapter = ArrayAdapter<Hotel>(
            requireContext(),
            android.R.layout.simple_list_item_activated_1,
            hotels
        )
        listAdapter = adapter
    }

    private fun showHotelDetail(hotel: Hotel) {
        if (activity is OnHotelClickListener) {
            val listener = activity as OnHotelClickListener
            listener.onHotelClick(hotel)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val hotel = l?.getItemAtPosition(position) as Hotel
        viewModel.selectHotel(hotel)
    }

    fun search(text: String = "") {
        viewModel.search(text)
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val consumed = (actionMode == null)
        if (consumed) {
            val hotel = parent?.getItemAtPosition(position) as Hotel
            viewModel.setInDeleteMode(true)
            viewModel.selectHotel(hotel)
        }
        return consumed
    }

    private fun showDeleteMode() {
        val appCompatActivity = (activity as AppCompatActivity)
        actionMode = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    fun hideDeleteMode() {
        listView.onItemLongClickListener = this
        for (i in 0 until listView.count) {
            listView.setItemChecked(i, false)
        }
        listView.post {
            actionMode?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }
    }

    private fun updateSelectionCountText(count: Int) {
        view?.post {
            actionMode?.title =
                resources.getQuantityString(R.plurals.list_hotel_selected, count, count)
        }
    }

    private fun showSelectedHotels(hotels: List<Hotel>) {
        listView.post {
            for (i in 0 until listView.count) {
                val hotel = listView.getItemAtPosition(i) as Hotel
                if (hotels.find { it.id == hotel.id } != null) {
                    listView.setItemChecked(i, true)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startSync()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(serviceReceiver, IntentFilter(HotelIntentService.ACTION_SYNC))
    }

    override fun onDetach() {
        super.onDetach()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(serviceReceiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_hotel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeColors(
            resources.getColor(R.color.colorPrimary),
            resources.getColor(R.color.colorAccent),
            resources.getColor(R.color.colorPrimaryDark)
        )
    }



    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete) {
            //presenter.deleteSelected { hotels ->
            viewModel.deleteSelected()

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
        viewModel.setInDeleteMode(false)
    }

    private fun showMessageHotelsDeleted(count: Int) {
        Snackbar.make(
            listView, getString(R.string.message_hotels_deleted, count),
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.undo) {
                viewModel.undoDelete()
            }
            .show()
    }


    interface OnHotelClickListener {
        fun onHotelClick(hotel: Hotel)
    }

    override fun onRefresh() {
        startSync()
    }

    private fun startSync() {
        JobIntentService.enqueueWork(
            requireContext(), HotelIntentService::class.java, 0,
            Intent(context, HotelIntentService::class.java)
        )
    }

    private val serviceReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                swipeRefresh.isRefreshing = false
                if (!intent!!.getBooleanExtra(HotelIntentService.EXTRA_SUCCESS, false)) {
                    Toast.makeText(activity, R.string.error_sync, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}