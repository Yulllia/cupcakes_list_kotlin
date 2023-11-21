package com.example.cupcakeslist.dessertDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.cupcakeslist.R
import com.example.cupcakeslist.utils.CommonUtils
import com.example.cupcakeslist.utils.DessertElFirebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

abstract class DetailFragment : Fragment() {
    private lateinit var detailAdapter: SingleAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var databaseRef: DatabaseReference

    abstract fun getFragmentLayoutId(): Int
    abstract fun getRecyclerViewId(): Int
    abstract fun getDessertType(): String
    abstract fun getFragment(): Fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getFragmentLayoutId(), container, false)
        val recyclerView: RecyclerView = view.findViewById(getRecyclerViewId())
        progressBar = view.findViewById(R.id.pgBar)
        val arrow: ImageView = view.findViewById(R.id.arrow_back)
        val position = arguments?.getInt("position", -1) ?: -1
        databaseRef = FirebaseDatabase.getInstance().getReference("desserts")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dessertItem = getDessertById(position)
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    detailAdapter = CommonUtils.setupDetailPageAndFetchData(
                        requireContext(),
                        recyclerView,
                        dessertItem,
                    )
                    arrow.setOnClickListener {
                        loadFragment(getFragment())
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", "Помилка при отриманні даних із Firebase: ${e.message}")
            }
        }
        return view
    }

    private suspend fun getDessertById(position: Int): DessertElFirebase {
        try {
            progressBar.visibility = View.VISIBLE
            val dataSnapshot = databaseRef.get().await()
            if (dataSnapshot.exists()) {
                var dessertList: MutableList<DessertElFirebase> = mutableListOf()
                for (childSnapshot in dataSnapshot.children) {
                    val contains = childSnapshot.child("contains").value.toString()
                    val imageName = childSnapshot.child("imageName").value.toString()
                    val ingredient = childSnapshot.child("ingredient").value.toString()
                    val price = childSnapshot.child("price").value.toString()
                    val selectedItem = childSnapshot.child("selectedItem").value.toString()
                    val containChoose = childSnapshot.child("containChoose").value.toString()
                    val title = childSnapshot.child("title").value.toString()
                    val key = childSnapshot.child("key").value.toString()

                    val imageList = childSnapshot.child("imageList").children.mapNotNull {
                        it.value.toString()
                    }
                    val dessert = DessertElFirebase(
                        key,
                        containChoose,
                        contains,
                        ingredient,
                        price,
                        title,
                        imageName,
                        selectedItem,
                        imageList
                    )
                    if(dessert.selectDesserts == getDessertType()){
                        dessertList.add(dessert)
                    }
                }
                return dessertList[position]
            }

        } catch (e: Exception) {
            Log.d("Error", "Error to get data from firebase")
        }
        return DessertElFirebase()
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}