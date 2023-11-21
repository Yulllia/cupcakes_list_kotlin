package com.example.trainingproject;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cupcakeslist.R
import com.example.cupcakeslist.utils.DessertElFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ListAdapter(
    private val context: Context,
    private var list: MutableList<DessertElFirebase>,
    private var textViewNoDessertFound: TextView
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var itemClickListener: ((Int) -> Unit)? = null
    private var user = FirebaseAuth.getInstance().currentUser

    companion object {
        const val USER_EMAIL = "shyshka20@gmail.com"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: Int
        var title: TextView
        var description: TextView
        var imageView: ImageView
        var deleteItem: ImageView

        init {
            id = itemView.id
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            imageView = itemView.findViewById(R.id.icon)
            deleteItem = itemView.findViewById(R.id.delete_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView =
            LayoutInflater.from(context).inflate(R.layout.custom_list_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageName = list[position].imageName
//        val bitmap = BitmapFactory.decodeByteArray(imageName, 0, imageName.size)
        Glide.with(context)
            .load(imageName)
            .into(holder.imageView)

        holder.title.text = list[position].title
        holder.description.text = list[position].containChoose
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position)
        }
        if (user?.email == USER_EMAIL) {
            holder.deleteItem.visibility = View.VISIBLE
            holder.deleteItem.setOnClickListener {
                val positionToDelete = holder.adapterPosition
                if (positionToDelete != RecyclerView.NO_POSITION) {
                    val dessertToDelete = list[position]
                    // Remove the item from the Firebase database
                    val databaseReference = FirebaseDatabase.getInstance().getReference("desserts")
                    databaseReference.child(dessertToDelete.key).removeValue()
                    // Optionally, update the local list and notify the adapter
                    list.removeAt(position)
                    if (list.isEmpty()) {
                        textViewNoDessertFound.visibility = View.VISIBLE
                    }
                    notifyItemRemoved(position)
                }
            }
        }
    }
    // Setter method to set the click listener
    fun setOnItemClickListener(listener: (Any) -> Unit) {
        itemClickListener = listener
    }

}