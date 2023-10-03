package com.example.mytodos.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodos.R
import com.example.mytodos.databinding.ItemPostBinding
import com.example.mytodos.entity.EntityPost

class TravelPostAdapter(private val itravelpostclick : ITravelPostClick) : RecyclerView.Adapter<TravelPostAdapter.TravelPostViewHolder>() {

    class TravelPostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    private var travelPosts=ArrayList<EntityPost>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelPostViewHolder {
        return TravelPostAdapter.TravelPostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: TravelPostViewHolder, position: Int) {
        val currTodo = travelPosts[position]
        holder.binding.postTitle.text=currTodo.posttitle
        holder.binding.postLocation.text = "Location : " + currTodo.location
        if(currTodo.imageUri != null)
        {
            val imageUri = Uri.parse(currTodo.imageUri)

            holder.binding.imagePost.setImageURI(imageUri)
        }
        holder.itemView.setOnClickListener {
            itravelpostclick.onPostItemClick(travelPosts[position])
        }

    }

    override fun getItemCount(): Int {
        return travelPosts.size
    }

    fun updateTravelPostList(updatedTravelPost: List<EntityPost>)
    {
        travelPosts.clear()
        travelPosts.addAll(updatedTravelPost)
        notifyDataSetChanged()
    }

}

interface ITravelPostClick {
    fun onPostItemClick(entityPost: EntityPost)
}
