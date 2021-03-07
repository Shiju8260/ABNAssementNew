package com.abn.amroassessment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.abn.amroassessment.BR
import com.abn.amroassessment.R
import com.abn.amroassessment.databinding.VenueItemBinding
import com.abn.amroassessment.model.venuesearchresponse.Venue
import java.util.*

class VenueAdapter(var onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<VenueAdapter.ViewHolder>() {

    private var venueList: MutableList<Venue> = mutableListOf()
    private var allVenueList: MutableList<Venue> = mutableListOf()

    //Loading data foe venue listafter API Response
    fun loadData(venueList: MutableList<Venue>) {
        this.venueList = venueList
        allVenueList.addAll(venueList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueAdapter.ViewHolder {
        val binding = DataBindingUtil.inflate<VenueItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.venue_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VenueAdapter.ViewHolder, position: Int) {
        val venue = venueList[position]
        holder.bind(venue)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    inner class ViewHolder(private var venueItemBinding: VenueItemBinding) :
        RecyclerView.ViewHolder(venueItemBinding.root) {
        fun bind(value: Venue) {
            venueItemBinding.setVariable(BR.model, value)
            venueItemBinding.executePendingBindings()
            venueItemBinding.venueCard.setOnClickListener {
                onItemClickListener.onItemClick(value)
            }
        }
    }

    //Based on search query filter out the list
    fun filter(charText: String) {
        venueList.clear()
        if (charText.length == 0) {
            venueList.addAll(allVenueList)
        } else {
            for (venue in allVenueList) {
                if (venue.name.toLowerCase(Locale.getDefault()).contains(charText.toLowerCase())) {
                    venueList.add(venue)
                }
            }
        }
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(venue: Venue)
    }

}