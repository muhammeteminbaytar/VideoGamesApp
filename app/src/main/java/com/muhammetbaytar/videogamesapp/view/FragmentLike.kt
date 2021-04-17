package com.muhammetbaytar.videogamesapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.muhammetbaytar.videogamesapp.CustomViewHolder
import com.muhammetbaytar.videogamesapp.GameList
import com.muhammetbaytar.videogamesapp.MainAdapter
import com.muhammetbaytar.videogamesapp.R
import kotlinx.android.synthetic.main.fragment_like.*
import okhttp3.*
import java.io.IOException

class FragmentLike : Fragment() {

    val arraylistLike = ArrayList<Int>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_like, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewLike.layoutManager = LinearLayoutManager(view.context)


    }

    fun getLikeId() {


        try {
            val db = activity?.openOrCreateDatabase("Likes", Context.MODE_PRIVATE, null)
            val cursor = db?.rawQuery("SELECT * FROM likes", null)

            val likeIndex = cursor?.getColumnIndex("likeId")

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    likeIndex?.let { cursor.getInt(it) }?.let { arraylistLike.add(it) }

                }
            }
            //println(arraylistLike.get(1))

        } catch (e: Exception) {
        }

    }

    fun loadGameData() {
        val client = OkHttpClient()

        val request = Request.Builder()
                .url("https://rawg-video-games-database.p.rapidapi.com/games")
                .get()
                .addHeader("x-rapidapi-key", "fc49f6e132msh060efd1094ef794p1b5619jsn4c1ca936cef8")
                .addHeader("x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Hata")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()

                val gson = GsonBuilder().create()
                val gameList = gson.fromJson(body, GameList::class.java)

                var game: Int = gameList.results.size - 1

                while (game >= 0) {
                    if (!arraylistLike.contains(gameList.results[game].id)) {
                        gameList.results.removeAt(game)
                    }
                    game -= 1

                }

                activity?.runOnUiThread {
                    recyclerViewLike.adapter = MainAdapter(gameList.results) {
                        openGameDetail(it)
                    }


                }
            }

        })
    }

    fun openGameDetail(gameId: Int) {
        val intent = Intent(requireContext(), GameDetailAct::class.java)

        intent.putExtra(CustomViewHolder.GAME_ID, gameId)

        requireContext().startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        getLikeId()
        loadGameData()
    }


}