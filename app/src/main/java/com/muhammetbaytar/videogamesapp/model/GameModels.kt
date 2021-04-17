package com.muhammetbaytar.videogamesapp.model

class GameList(var results: MutableList<Games>)

class Games(val id: Int, val name: String, val released: String, val background_image: String, val rating: Double)