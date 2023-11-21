package com.example.cupcakeslist.addDessert

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.cupcakeslist.R

class ImageAdapter(private val context: Context, private val intArray: IntArray?, var uriList: MutableList<Uri>?, var imageDetails: Array<String>?, private val viewPager: ViewPager?, private val saveDessert: Button?): PagerAdapter() {

    private val mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var itemClickListener: ((Int) -> Unit)? = null


    override fun getCount(): Int {
        // Return the number of images
        return intArray?.size ?: uriList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // Inflating the item.xml
        val itemView = mLayoutInflater.inflate(R.layout.item_image, container, false)

        // Referencing the image view from the item.xml file
        val imageView = itemView.findViewById<ImageView>(R.id.imageViewMain)
        val removeIcon = itemView.findViewById<ImageView>(R.id.deleteImage)

        if (intArray != null && position < intArray.size) {
            // If the intArray is not null and the position is within its bounds
            imageView.setImageResource(intArray[position])
            removeIcon.visibility = View.GONE
        } else if(imageDetails!==null){
            val imageUri = imageDetails!![position]

            Glide.with(imageView)
                .load(imageUri)
                .into(imageView)
            removeIcon.visibility = View.GONE

            itemView.setOnClickListener {
                itemClickListener?.invoke(position)
            }
            container.addView(itemView)
        } else if (uriList != null && position <= uriList!!.size) {
            imageView.setImageResource(R.drawable.upload_files)
            // If the uriList is not null and the position is within its bounds
            imageView.setImageURI(uriList!![position])
            removeIcon.visibility = View.VISIBLE
            saveDessert?.visibility = View.VISIBLE
            removeIcon.setOnClickListener {
                if (position >= 0 && position < uriList!!.size) {
                    // Remove the item from the list
                    uriList!!.removeAt(position)

                    notifyDataSetChanged()

                    if (uriList!!.isEmpty()) {
                        // If the list is empty after removal, clear the image and hide the icon
                        imageView.setImageURI(null)
                        removeIcon.visibility = View.GONE
                        saveDessert?.visibility = View.GONE
                    } else if (position < uriList!!.size) {
                        // If there is an item after the removed one, move to the next item
                        viewPager?.setCurrentItem(position, true)
                        imageView.setImageURI(uriList!![position])// Use setCurrentItem to scroll to the specified item
                    } else {
                        // If the removed item was the last one, move to the previous item
                        viewPager?.setCurrentItem(position - 1, true)
                        imageView.setImageURI(uriList!![position-1])
                    }
                    notifyDataSetChanged()
                }
            }

        }
        // Adding the View
        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}

