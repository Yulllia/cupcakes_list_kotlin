package com.example.cupcakeslist
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cupcakeslist.addDessert.AdminAddDessert
import com.example.cupcakeslist.authenticationFirebase.SignUpActivity
import com.example.cupcakeslist.fragments.ContactFragment
import com.example.cupcakeslist.fragments.HomeFragment
import com.example.cupcakeslist.fragments.InstagramFragment
import com.example.cupcakeslist.fragments.MySelfFragment
import com.example.cupcakeslist.navbar.BaseNavbar
import com.example.cupcakeslist.server.Email
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity: BaseNavbar() {

    private lateinit var bottomNav : BottomNavigationView
    private val sharedPrefsKey = "SIGNUP_LAUNCHED"
    private lateinit var sharedPrefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        // Initialize SharedPreferences
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        //  Check if SignUpActivity has been launched before
        val signUpLaunchedBefore = sharedPrefs.getBoolean(sharedPrefsKey, false)

        if (!signUpLaunchedBefore) {
            // Load the SignUpActivity initially
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

            // Update shared preference to indicate that SignUpActivity has been launched
            sharedPrefs.edit().putBoolean(sharedPrefsKey, true).apply()
        } else {
            // Load your HomeFragment if needed
            loadFragment(HomeFragment())
        }

        val user = FirebaseAuth.getInstance().currentUser

        //check if user is admin
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        val menu = bottomNavigationView.menu
        val addItem = menu.findItem(R.id.add_dessert)

        addItem.isVisible = user?.email == Email.USER_EMAIL

        bottomNav = findViewById(R.id.nav_view)

        //on menu selected navigation
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.desserts -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.contacts -> {
                    loadFragment(ContactFragment())
                    true
                }
                R.id.my_self -> {
                    loadFragment(MySelfFragment())
                    true
                }
                R.id.instagram -> {
                    loadFragment(InstagramFragment())
                    true
                }
                R.id.add_dessert -> {
                    loadFragment(AdminAddDessert())
                    true
                }
                else -> true
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
  }
}
