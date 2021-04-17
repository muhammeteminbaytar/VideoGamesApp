package com.muhammetbaytar.videogamesapp.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.GsonBuilder
import com.muhammetbaytar.videogamesapp.R
import com.muhammetbaytar.videogamesapp.adapter.CustomViewHolder
import com.muhammetbaytar.videogamesapp.adapter.MainAdapter
import com.muhammetbaytar.videogamesapp.adapter.ViewPagerAdapter
import com.muhammetbaytar.videogamesapp.model.GameList
import com.muhammetbaytar.videogamesapp.model.Games
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import java.io.IOException

class FragmentHome : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewMain.layoutManager = LinearLayoutManager(view.context)

        loadGameData()


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

                makeSearch(gameList)//arama işlemi için fun a list gönderir


                activity?.runOnUiThread {
                    recyclerViewMain.adapter = MainAdapter(gameList.results.drop(3).toMutableList()) {

                      openGameDetail(it)
                    }


                    val adapter = ViewPagerAdapter(gameList.results.take(3)) {

                        openGameDetail(it)
                    }
                    viewpager.adapter = adapter

                    viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                }
            }

        })
    }
    fun openGameDetail(gameId:Int){
        val intent = Intent(requireContext(), GameDetailAct::class.java)

        intent.putExtra(CustomViewHolder.GAME_ID,gameId)

        requireContext().startActivity(intent)

    }

    fun makeSearch(gameList: GameList) {

        home_txt_search?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var filterList:List<Games>?
                if(count<3){
                    filterList=gameList.results
                    viewpager.visibility=View.VISIBLE

                }else{
                    viewpager.visibility=View.GONE
                    filterList = gameList.results.filter {
                        it.name.contains(s.toString(),ignoreCase = true)
                    }
                }

                recyclerViewMain.adapter = MainAdapter(filterList.toMutableList()) {

                    openGameDetail(it)
                }


            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


}