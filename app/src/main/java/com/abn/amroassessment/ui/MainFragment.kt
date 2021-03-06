package com.abn.amroassessment.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.abn.amroassessment.R
import com.abn.amroassessment.adapter.VenueAdapter
import com.abn.amroassessment.database.VenueRoomDatabase
import com.abn.amroassessment.databinding.MainFragmentBinding
import com.abn.amroassessment.model.venuesearchresponse.Venue
import com.abn.assessment.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment(), VenueAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var venueAdapter: VenueAdapter
    private lateinit var navController: NavController
    private lateinit var db: VenueRoomDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(container?.context),
            R.layout.main_fragment,
            container,
            false
        )
        venueAdapter = VenueAdapter(this)
        navController = NavHostFragment.findNavController(this)
        db = activity?.let { VenueRoomDatabase.getDatabase(it.applicationContext) }!!
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getVenueList(db, isNetworkAvailable((context)))
        binding.venueList.adapter = venueAdapter
        binding.venueList.layoutManager = LinearLayoutManager(activity)
        binding.venueList.addItemDecoration(
            DividerItemDecoration(
                binding.venueList.context, 0
            )
        )
        viewModel.mVenueSearchResponseLiveData.observeForever {
            if (it.size > 0) {
                binding.venueList.visibility = View.VISIBLE
                binding.txtEmptyMessage.visibility = View.GONE
                viewModel.storeVenueListInDB(db, it)
                venueAdapter.loadData(venueList = it)
            } else {
                binding.txtEmptyMessage.visibility = View.VISIBLE
                binding.venueList.visibility = View.GONE
            }
        }
        viewModel.mProgressBarVisibilityLiveData.observeForever {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.mApiErrorMessageLiveData.observeForever {
            Snackbar.make(view, it, Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                venueAdapter.filter(newText ?: "")
                return true
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(venue: Venue) {
        val action = MainFragmentDirections.venueDetailsAction(venue.venueUniqId)
        navController.navigate(action)
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

}
