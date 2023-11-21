package com.example.cupcakeslist.addDessert

import com.example.cupcakeslist.enumData.DessertOption

data class DessertAdd(
    val key: String,
    val containChoose: String,
    val contains: String,
    val ingredient: String,
    val price: String,
    val title: String,
    val imageName: String?,
    val selectedItem: DessertOption?,
    val imageList: MutableList<String>
)

