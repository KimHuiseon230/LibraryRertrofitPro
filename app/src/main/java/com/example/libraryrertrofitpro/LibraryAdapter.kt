package com.example.libraryrertrofitpro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryrertrofitpro.databinding.ItemLayoutBinding

class LibraryAdapter (val libraryList: MutableList<LibraryData>):RecyclerView.Adapter<LibraryAdapter.CustomViewholder>(){

    inner class CustomViewholder (val binding: ItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewholder {
        val binding =ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CustomViewholder(binding)
    }

    override fun getItemCount(): Int  = libraryList.size

    override fun onBindViewHolder(holder: CustomViewholder, position: Int) {
      val binding = holder.binding
        binding.tvGuCode.text = libraryList.get(position).code
        binding.tvLbName.text = libraryList.get(position).name
        binding.tvTelNo.text = libraryList.get(position).phone
        binding.tvAddress.text = libraryList.get(position).address
        binding.tvLatitude.text = libraryList.get(position).latitude
        binding.tvLongitude.text = libraryList.get(position).longitude
    }
}