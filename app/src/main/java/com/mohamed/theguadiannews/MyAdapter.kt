package com.mohamed.theguadiannews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.theguadiannews.databinding.RvItemBinding
import com.squareup.picasso.Picasso

class MyAdapter(
    private val items: ArrayList<Model>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    ///// ViewHolder class using ViewBinding instead of View
    class ViewHolder(binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTitle
        val date = binding.tvDate
        val iv = binding.iv


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        ///// Assign the views value
        holder.title.text = item.webTitle
        holder.date.text = item.webPublicationDate

        val imageTarget = item.thumbnail
        if (imageTarget.isNotEmpty()) {
            Picasso.get().load(imageTarget).fit().into(holder.iv)
        } else {
            Picasso.get().load(R.drawable.no_image).fit().into(holder.iv)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}