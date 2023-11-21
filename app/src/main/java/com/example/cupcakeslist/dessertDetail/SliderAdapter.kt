package com.example.cupcakeslist.dessertDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.cupcakeslist.R
import com.smarteist.autoimageslider.SliderViewAdapter

// on below line we are creating a class for slider
// adapter and passing our array list to it.
class SliderAdapter(private val list: List<String?>) : SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    class SliderViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        val sliderIV: ImageView = itemView.findViewById(R.id.myImage)
    }

    override fun getCount(): Int {
        return list.size
    }

    // on below line we are calling on create view holder method.
    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        // inside this method we are inflating our layout file for our slider view.
        val inflate: View = LayoutInflater.from(parent!!.context).inflate(R.layout.fragment_image_preview, null)
        return SliderViewHolder(inflate)
    }

    // on below line we are calling on bind view holder method to set the data to our image view.
    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        Glide.with(viewHolder!!.itemView).load(list[position]).fitCenter()
            .into(viewHolder.sliderIV)
        }

    }
