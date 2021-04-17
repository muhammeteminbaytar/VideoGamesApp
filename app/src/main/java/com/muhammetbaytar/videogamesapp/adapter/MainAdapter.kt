package com.muhammetbaytar.videogamesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_row.view.*

class MainAdapter(val gameList: MutableList<Games>,val onItemClickCallBack:(Int)->Unit): RecyclerView.Adapter<CustomViewHolder>(){


    override fun getItemCount(): Int {
        return gameList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.game_row,parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val game=gameList.get(position)
        holder?.view?.row_txt_name?.text=game.name
        holder?.view?.row_txt_rating?.text=game.rating.toString()
        holder?.view?.row_txt_released?.text=game.released

        val GameImgVw=holder?.view?.row_image
        Picasso.with(holder?.view?.context).load(game.background_image).into(GameImgVw)

        holder?.game=game

        holder.itemView.setOnClickListener {
            onItemClickCallBack.invoke(game.id)
        }
    }

}
class CustomViewHolder(val view:View,var game:Games?=null): RecyclerView.ViewHolder(view) {

    companion object{
        val GAME_ID="GAME_ID"
    }



}