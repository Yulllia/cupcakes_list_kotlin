package com.example.cupcakeslist.utils

data class DessertElFirebase @JvmOverloads constructor(
    val key: String = "",
    val containChoose: String = "",
    val contains: String = "",
    val ingredient: String = "",
    val price: String = "",
    val title: String = "",
    val imageName: String? = "",
    val selectDesserts: String = "",
    var imageList: List<String?> = mutableListOf()
)
