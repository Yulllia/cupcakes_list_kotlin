package com.example.cupcakeslist.navbar
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cupcakeslist.fragments.Delivery
import com.example.cupcakeslist.R
import com.example.cupcakeslist.dessertsList.CupcakesFragment
import com.example.cupcakeslist.dessertsList.MacaroonsFragment

open class BaseNavbar: AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_macaroons -> loadFragment(MacaroonsFragment())
            R.id.action_cupcakes -> loadFragment(CupcakesFragment())
            R.id.action_pay -> loadFragment(Delivery())
        }
        return false
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}