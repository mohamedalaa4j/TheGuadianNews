package com.mohamed.theguadiannews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.theguadiannews.databinding.RvItemBinding
import com.squareup.picasso.Picasso

class MyAdapter(
    private val items: ArrayList<Model>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    ///// Listener

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }
    ///

    ///// ViewHolder class using ViewBinding instead of View
    class ViewHolder(binding: RvItemBinding, listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTitle
        val date = binding.tvDate
        val iv = binding.iv

        init {
            itemView.setOnClickListener {

                listener.onItemClick(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),mListener)
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