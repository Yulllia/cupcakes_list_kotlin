package com.example.cupcakeslist.dessertDetail;
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cupcakeslist.R
import com.example.cupcakeslist.utils.DessertElFirebase
import com.smarteist.autoimageslider.SliderView

class SingleAdapter(private val context: Context, private var item: DessertElFirebase):
    RecyclerView.Adapter<SingleAdapter.ViewHolder>() {

    private lateinit var sliderAdapter: SliderAdapter
    private var itemClickListener: ((position: Int) -> Unit)? = null

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title)
        val ingredient: TextView = itemView.findViewById(R.id.ingredient)

        val filledInside: TextView = itemView.findViewById(R.id.filledInside)
        val contains: TextView = itemView.findViewById(R.id.containsInside)
        val price: TextView = itemView.findViewById(R.id.price)
        var sliderView: SliderView = itemView.findViewById(R.id.sliderImageDetail) // Add this line
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.detail_cupcakes, parent, false)
        val viewHolder = ViewHolder(itemView)
        viewHolder.sliderView = itemView.findViewById(R.id.sliderImageDetail) // Replace 'your_slider_view_id' with the actual ID
        return viewHolder
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val fullTextIngredient = "Інгредієнти: " + item.ingredient
        val fullTextContains = "Склад: " + item.contains
        val fullTextFilledInside = "Склад на ваш вибір: " + item.containChoose
        val fullTextPrice = "Ціна: " + item.price

        val spannableIngredient = SpannableString(fullTextIngredient)
        val spannableContains = SpannableString(fullTextContains)
        val spannableFilledInside = SpannableString(fullTextFilledInside)
        val spannablePrice = SpannableString(fullTextPrice)

        val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
        val colorPrimarySpan = ForegroundColorSpan(colorPrimary)

        spannableIngredient.setSpan(colorPrimarySpan, 0, "Інгредієнти: ".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableContains.setSpan(colorPrimarySpan, 0, "Склад: ".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableFilledInside.setSpan(colorPrimarySpan, 0, "Склад на ваш вибір: ".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannablePrice.setSpan(colorPrimarySpan, 0, "Ціна: ".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        holder.title.text = item.title
        holder.ingredient.text = spannableIngredient
        holder.contains.text = spannableContains
        holder.filledInside.text = spannableFilledInside
        holder.price.text = spannablePrice

        val sliderView: SliderView = holder.sliderView

        sliderAdapter = SliderAdapter(item.imageList)

        // on below line we are setting auto cycle direction
        // for our slider view from left to right.
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        // on below line we are setting adapter for our slider.
        sliderView.setSliderAdapter(sliderAdapter)

        // on below line we are setting scroll time
        // in seconds for our slider view.
        sliderView.scrollTimeInSec = 25

        // on below line we are setting auto cycle
        // to true to auto slide our items.
        sliderView.isAutoCycle = true

        // on below line we are calling start
        // auto cycle to start our cycle.
        sliderView.startAutoCycle()

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position)
        }
    }
}
