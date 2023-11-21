package com.example.cupcakeslist.utils
import android.content.Context
import android.widget.TextView

import com.example.trainingproject.ListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cupcakeslist.dessertDetail.SingleAdapter


object CommonUtils {
    fun setupRecyclerViewAndFetchData(
        context: Context,
        recyclerView: RecyclerView,
        dessertList: MutableList<DessertElFirebase>,
        textViewNoDessertFound: TextView
    ): ListAdapter {
        recyclerView.setHasFixedSize(true)

        val adapter = ListAdapter(context, dessertList, textViewNoDessertFound)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        return adapter
    }
    fun setupDetailPageAndFetchData(
        context: Context,
        recyclerView: RecyclerView,
        databaseHelper: DessertElFirebase,
    ): SingleAdapter {
        recyclerView.setHasFixedSize(true)

        val adapter = SingleAdapter(context, databaseHelper)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        return adapter
    }


}