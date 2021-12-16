package com.example.postrequestpractice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postrequestpractice.databinding.ItemRowBinding

class NameLocationAdapter(private var namesLocations: NamesAndLocations):
    RecyclerView.Adapter<NameLocationAdapter.NameLocationViewHolder>() {
    class NameLocationViewHolder(val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameLocationViewHolder {
        return NameLocationViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NameLocationViewHolder, position: Int) {
        val nameLocat = namesLocations[position]

        holder.binding.apply {
            nameTV.text = "Name: ${nameLocat.name}"
            locationTV.text = "Location: ${nameLocat.location}"
            pkTV.text = "PK: ${nameLocat.pk}"
        }
    }

    override fun getItemCount() = namesLocations.size

    fun updateRecyclerView(userInput: NamesAndLocations) {
        this.namesLocations = userInput
        notifyDataSetChanged()
    }
}