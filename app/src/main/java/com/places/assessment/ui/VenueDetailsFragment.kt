package com.places.assessment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.places.assessment.R
import com.places.assessment.databinding.VenuedetailsFragmentBinding
import com.places.assessment.viewmodel.VenueDetailsViewModel

//Venue Details
class VenueDetailsFragment : Fragment() {

    private lateinit var binding: VenuedetailsFragmentBinding
    private lateinit var viewModel: VenueDetailsViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(container?.context),
            R.layout.venuedetails_fragment,
            container,
            false
        )
        navController = NavHostFragment.findNavController(this)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VenueDetailsViewModel::class.java)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        //Getting venue details based on clicked venue id
        viewModel.getVenueDetails(arguments?.getString("venueId"))
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.photo.observeForever {
            binding.backdrop.load(it) {
                placeholder(R.drawable.ic_launcher_background)
            }
        }
        viewModel.mProgressBarVisibilityLiveData.observeForever {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        viewModel.mResultSuccessLiveData.observeForever {
            if (it) {
                binding.mainContent.visibility = View.VISIBLE
                binding.txtEmptyMessage.visibility = View.GONE
            } else {
                binding.txtEmptyMessage.visibility = View.VISIBLE
                binding.mainContent.visibility = View.GONE
            }
        }
        viewModel.mApiErrorMessageLiveData.observeForever {
            Snackbar.make(view, it, Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

}