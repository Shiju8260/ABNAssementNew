package com.abn.amroassessment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.abn.amroassessment.R
import com.abn.amroassessment.databinding.VenuedetailsFragmentBinding
import com.abn.amroassessment.viewmodel.VenueDetailsViewModel
import com.abn.assessment.viewmodel.MainViewModel

class VenueDetailsFragment : Fragment() {

    private lateinit var binding: VenuedetailsFragmentBinding
    private lateinit var viewModel: VenueDetailsViewModel

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(VenueDetailsViewModel::class.java)
        viewModel.getVenueDetails(arguments?.getString("venueId"))
        binding.viewModel = viewModel
        binding.lifecycleOwner= this
        viewModel.photo.observeForever {
            binding.backdrop.load(it){
                placeholder(R.drawable.ic_launcher_background)
            }
        }

    }
}