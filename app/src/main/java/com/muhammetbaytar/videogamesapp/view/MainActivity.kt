package com.muhammetbaytar.videogamesapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.muhammetbaytar.videogamesapp.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        clickBottomNav()


    }
    fun clickBottomNav() {

        supportFragmentManager.beginTransaction().add(R.id.fragment_object, FragmentHome()).commit()

        bottom_navi.setOnNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.act_home) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_object, FragmentHome()).commit()
            }
            if (menuItem.itemId == R.id.act_like) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_object, FragmentLike()).commit()
            }
            true

        }}


}

