package com.muhammetbaytar.videogamesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammetbaytar.videogamesapp.R
import com.muhammetbaytar.videogamesapp.model.Games
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_pager.view.*

class ViewPagerAdapter(
        val games: List<Games>,
        private val onItemClickCallBack:(Int)->Unit
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val curGame = games[position]
        Picasso.with(holder.itemView.iv_img.context).load(curGame.background_image).into(holder.itemView.iv_img)

        holder.itemView.setOnClickListener {
            onItemClickCallBack.invoke(curGame.id)
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }
}