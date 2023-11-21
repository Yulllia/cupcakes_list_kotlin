package com.example.cupcakeslist.addDessert

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.cupcakeslist.R
import com.example.cupcakeslist.enumData.DessertOption
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AdminAddDessert : Fragment() {
    private lateinit var BSelectImage: Button
    lateinit var imageAdapter: ImageAdapter
    private lateinit var viewPager: ViewPager
    private val imageUris: MutableList<Uri> = ArrayList()

    //    private lateinit var databaseHelper: DataBaseHelperContent
    private lateinit var databaseRef: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private lateinit var addTitle: EditText
    private lateinit var addTitleLayout: TextInputLayout

    private lateinit var contains: EditText
    private lateinit var containsLayout: TextInputLayout

    private lateinit var ingredientsLayout: TextInputLayout
    private lateinit var ingredientsDessert: EditText

    private lateinit var containsDessertChoice: TextInputLayout
    private lateinit var containsDessert: EditText

    private lateinit var price: EditText
    private lateinit var priceLayout: TextInputLayout

    private lateinit var select: Spinner

    private lateinit var storage: StorageReference
    private lateinit var saveDessert: Button

    companion object {
        private const val PICK_IMAGES = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_dessert, container, false)

        // register the UI widgets with their appropriate IDs
        BSelectImage = view.findViewById(R.id.BSelectImage)
        saveDessert = view.findViewById(R.id.saveDessert)

        databaseRef = FirebaseDatabase.getInstance().getReference("desserts")

        viewPager = view.findViewById(R.id.imageViewAdd)

        imageAdapter = ImageAdapter(requireContext(), null, imageUris, null, viewPager, saveDessert)
        viewPager.adapter = imageAdapter

        imageAdapter.notifyDataSetChanged()
        //title
        addTitle = view.findViewById(R.id.editTextTitleDessert)
        addTitleLayout = view.findViewById(R.id.addTitleDessert)
        //contains
        containsLayout = view.findViewById(R.id.addInside)
        contains = view.findViewById(R.id.editTextAddInside)

        //ingredient
        ingredientsDessert = view.findViewById(R.id.editTextIngredients)
        ingredientsLayout = view.findViewById(R.id.ingredients)

        //contains dessert
        containsDessertChoice = view.findViewById(R.id.containsInside)
        containsDessert = view.findViewById(R.id.editTextContainsInside)

        //price
        price = view.findViewById(R.id.editTextPrice)
        priceLayout = view.findViewById(R.id.price)

        select = view.findViewById(R.id.spinnerDesserts)

        fun EditText.addValidationTextWatcher(
            layout: TextInputLayout,
            validationLambda: (String) -> Pair<Boolean, String>
        ) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    val (isValid, errorMessage) = validationLambda(text.toString().trim())
                    layout.error = if (!isValid) errorMessage else null
                }
            })
        }
        val fieldsToValidate = listOf(
            Pair(addTitle, addTitleLayout),
            Pair(contains, containsLayout),
            Pair(ingredientsDessert, ingredientsLayout),
            Pair(containsDessert, containsDessertChoice),
            Pair(price, priceLayout)
        )
        var allFieldsValid = true
        fieldsToValidate.forEach { (editText, textInputLayout) ->
            editText.addValidationTextWatcher(textInputLayout) { text ->
                val (isValid, errorMessage) = when {
                    text.isEmpty() -> Pair(false, "Пусте поле")
                    else -> Pair(true, "")
                }
                textInputLayout.error = if (!isValid) errorMessage else null
                allFieldsValid = isValid
                // Update the overall validation status
                saveDessert.isEnabled = allFieldsValid // Enable/disable the button

                Pair(isValid, errorMessage)
            }
        }
        //storage image
        storage = FirebaseStorage.getInstance().getReference("images")

        // handle the Choose Image button to trigger
        // the image chooser function
        BSelectImage.setOnClickListener {
            imageAdapter =
                ImageAdapter(requireContext(), null, imageUris, null, viewPager, saveDessert)
            viewPager.adapter = imageAdapter

            imageAdapter.notifyDataSetChanged()

            val intent = Intent()
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGES)
        }
        var imageArray: MutableList<ByteArray?> = mutableListOf()

        saveDessert.setOnClickListener {
            val titleDessert = addTitle.text.toString()
            val containsItem = contains.text.toString()
            val ingredients = ingredientsDessert.text.toString()
            val containsToChoose = containsDessert.text.toString()
            val priceDessert = price.text.toString()
            var imageList: MutableList<String> = mutableListOf()

            val selectedItem: DessertOption = when (select.selectedItem.toString()) {
                "Макаруни" -> DessertOption.MACAROONS
                "Капкейки" -> DessertOption.CUPCAKES
                else -> DessertOption.MACAROONS // Provide a default value or handle unknown selections
            }

            imageUris.forEach { el ->
                val inputStream = requireContext().contentResolver.openInputStream(el)
                val bytes = inputStream?.readBytes()
                imageArray.add(bytes)
            }

            suspend fun uploadImage(imageUri: Uri): String? {
                val storageReference = FirebaseStorage.getInstance().getReference("uploads")
                val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")
                val uploadTask = imageRef.putFile(imageUri)

                return try {
                    val imageSnapshot = uploadTask.await()
                    val uri = imageSnapshot.storage.downloadUrl.await()
                    uri.toString()
                } catch (e: Exception) {
                    Log.e("FirebaseStorage", "Error uploading image: $e")
                    null
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                val imageUrlList = ArrayList<String>()
                // Use async to upload images in parallel
                val uploadJobs = imageUris.map { imageUri ->
                    async {
                        uploadImage(imageUri)
                    }
                }
                // Wait for all uploads to complete and collect the download URLs
                imageUrlList.addAll(uploadJobs.mapNotNull { it.await() })
                val dessertId = databaseRef.push().key!!;
                // Create the DessertAdd object with the list of image URLs
                val dessert = DessertAdd(
                    key = dessertId,
                    containChoose = containsToChoose,
                    contains = containsItem,
                    ingredient = ingredients,
                    price = priceDessert,
                    title = titleDessert,
                    imageName = imageUrlList[0],
                    selectedItem = selectedItem,
                    imageList = imageUrlList,
                )
                if (allFieldsValid) {
                    addDessert(dessert)
                }else{
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Заповніть усі поля", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }

    private fun addDessert(dessert: DessertAdd) {
            databaseRef.child(dessert.key).setValue(dessert)
                .addOnCompleteListener {
                    Toast.makeText(requireContext(), "Дані успішно збережено", Toast.LENGTH_SHORT)
                        .show()
                    addTitle.text.clear()
                    contains.text.clear()
                    ingredientsDessert.text.clear()
                    containsDessert.text.clear()
                    price.text.clear()
                    imageUris.clear()
                    imageAdapter.uriList = imageUris
                    imageAdapter =
                        ImageAdapter(requireContext(), null, null, null, viewPager, saveDessert)
                    viewPager.adapter = imageAdapter
                    imageAdapter.notifyDataSetChanged()
                    saveDessert.visibility = View.GONE
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageUris.clear()
        if (requestCode == PICK_IMAGES && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                // Multiple images were selected
                val clipData = data.clipData
                if (clipData !== null) {
                    saveDessert.visibility = View.VISIBLE
                    val desiredHeight = resources.getDimension(R.dimen.desired_image_height)
                    viewPager.layoutParams.height = desiredHeight.toInt()
                    viewPager.requestLayout()
                } else {
                    saveDessert.visibility = View.GONE
                }
                for (i in 0 until clipData!!.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    // Display each selected image
                    imageUris.add(imageUri)
                }
            } else if (data?.data != null) {
                // Single image was selected
                val imageUri = data.data
                // Display the selected image
                if (imageUri != null) {
                    imageUris.add(imageUri)
                }
            }
        }
        imageAdapter.notifyDataSetChanged()
    }
}





