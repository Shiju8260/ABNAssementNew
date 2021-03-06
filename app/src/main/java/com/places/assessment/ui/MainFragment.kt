package com.places.assessment.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.places.assessment.R
import com.places.assessment.adapter.VenueAdapter
import com.places.assessment.database.VenueRoomDatabase
import com.places.assessment.databinding.MainFragmentBinding
import com.places.assessment.model.venuesearchresponse.Venue
import com.places.assessment.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.staralliance.networkframework.NetworkManager

//Venue List
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
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = VenueRoomDatabase.getDatabase(requireContext())
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getVenueList(db.venueDao(), NetworkManager.isNetworkConnected(requireContext()))
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
                viewModel.storeVenueListInDB(db.venueDao(), it)
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

        viewModel.mDataErrorMessageLiveData.observeForever {
            Snackbar.make(view, it, Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        //SearchView on menu
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.txt_Search)
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

    //OnItem click listener for venue list
    override fun onItemClick(venue: Venue) {
        val action = MainFragmentDirections.venueDetailsAction(venue.venueUniqId)
        navController.navigate(action)
    }

}
