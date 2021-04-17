package com.muhammetbaytar.videogamesapp.view

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.muhammetbaytar.videogamesapp.R
import com.muhammetbaytar.videogamesapp.adapter.CustomViewHolder
import com.muhammetbaytar.videogamesapp.model.Details
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game_detail.*
import okhttp3.*
import java.io.IOException

@Suppress("DEPRECATION")
class GameDetailAct : AppCompatActivity() {

    var liked:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)
        val gameID=intent.getIntExtra(CustomViewHolder.GAME_ID,28)


        loadDetailData(gameID)
        likeControl(gameID)
        showLike(gameID)
    }

    fun showLike(LikeId: Int){
        try {
            val likeDataBase = this.openOrCreateDatabase("Likes", MODE_PRIVATE, null)
            likeDataBase.execSQL("CREATE TABLE IF NOT EXISTS likes(likeId INT)")

            val cursor = likeDataBase.rawQuery("SELECT * FROM likes", null)


            val idIx = cursor.getColumnIndex("likeId")

            while (cursor.moveToNext()) {
                println("ID " + cursor.getInt(idIx))
                if(LikeId==cursor.getInt(idIx)){
                    detail_btn_like.setColorFilter(Color.argb(255, 204, 0, 0));
                    liked=true
                }
            }

            cursor.close()
      }catch (e:java.lang.Exception){}



    }

    fun likeControl(LikeId:Int){

        detail_btn_like.setOnClickListener {
            if(liked==false) {
                detail_btn_like.setColorFilter(Color.argb(255, 204, 0, 0));
                liked=true
                Toast.makeText(this, "Added To Favorites", Toast.LENGTH_SHORT).show()
                likeDataBaseControl(LikeId,true)
            }else{
                detail_btn_like.setColorFilter(Color.argb(255, 113, 113, 113));
                liked=false
                Toast.makeText(this, "Removed From Favorites", Toast.LENGTH_SHORT).show()
                likeDataBaseControl(LikeId,false)
            }
        }
    }
    fun likeDataBaseControl(LikeId:Int,SelectControl:Boolean){

        val likeDataBase = this.openOrCreateDatabase("Likes", MODE_PRIVATE, null)

        likeDataBase.execSQL("CREATE TABLE IF NOT EXISTS likes(likeId INT)")
        if(SelectControl==true) {

            try {

                likeDataBase.execSQL("INSERT INTO likes (likeId) VALUES ($LikeId)")


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
            try {
                likeDataBase.execSQL("DELETE FROM likes WHERE likeId=$LikeId")

            }catch (e:java.lang.Exception){
            }
        }




    }
    fun loadDetailData(indexid:Int){
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://rawg-video-games-database.p.rapidapi.com/games/"+indexid.toString())
            .get()
            .addHeader("x-rapidapi-key", "fc49f6e132msh060efd1094ef794p1b5619jsn4c1ca936cef8")
            .addHeader("x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Detail Hata")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()

                val gson= GsonBuilder().create()
                val detailList=gson.fromJson(body,Details::class.java)

                runOnUiThread{
                    detail_txt_name.text=detailList.name
                    detail_txt_rate.text="Metacritic Rate : "+detailList.metacritic.toString()
                    detail_txt_released.text= "Release Date : "+detailList.released
                    detail_txt_desc.text=Html.fromHtml( detailList.description)

                    Picasso.with(this@GameDetailAct).load(detailList.background_image).into(detail_img)

                }
            }

        })
    }
}