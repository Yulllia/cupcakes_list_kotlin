package com.example.cupcakeslist.dessertsList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.cupcakeslist.R
import com.example.cupcakeslist.utils.CommonUtils
import com.example.cupcakeslist.utils.DessertElFirebase
import com.example.trainingproject.ListAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

abstract class DessertListAbstract: Fragment() {

    private lateinit var listAdapter: ListAdapter
    private lateinit var databaseRef: DatabaseReference
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewNoDessertFound: TextView

    abstract fun getLayoutResource(): Int

    abstract fun getListDessert(): Int
    abstract fun dessertDetailFragment(position: Int): Fragment
    abstract var dessertSelection: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutResource(), container, false)
        val recyclerView: RecyclerView = view.findViewById(getListDessert())
        progressBar = view.findViewById(R.id.pgBar)
        textViewNoDessertFound = view.findViewById(R.id.textViewNoDessertFound)
        databaseRef = FirebaseDatabase.getInstance().getReference("desserts")
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dessertList = getDessert()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if(dessertList.isEmpty()){
                        recyclerView.visibility = View.GONE
                        textViewNoDessertFound.visibility = View.VISIBLE
                    }
                    listAdapter = CommonUtils.setupRecyclerViewAndFetchData(
                        requireContext(),
                        recyclerView,
                        dessertList,
                        textViewNoDessertFound
                    )
                    listAdapter.setOnItemClickListener { position->
                        val fragment = dessertDetailFragment(position as Int)
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.container, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", "Помилка при отриманні даних із Firebase: ${e.message}")
            }
        }
        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    private suspend fun getDessert(): MutableList<DessertElFirebase> {
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
                    if(dessert.selectDesserts == dessertSelection){
                        dessertList.add(dessert)
                    }
                }
                return dessertList
            }

        } catch (e: Exception) {
            Log.d("Error", "Error to get data from firebase")
        }
        return mutableListOf()
    }
}