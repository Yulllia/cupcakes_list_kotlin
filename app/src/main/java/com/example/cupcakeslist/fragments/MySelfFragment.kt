package com.example.cupcakeslist.fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cupcakeslist.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MySelfFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_self, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contacts = view.findViewById<TextView>(R.id.titleContacts)
        contacts.movementMethod = LinkMovementMethod.getInstance()


        contacts.setOnClickListener {
            fragmentManager?.let { manager ->
                val fragment = ContactFragment()
                val transaction = manager.beginTransaction()

                // Verify that R.id.container is the correct container ID
                transaction.replace(R.id.container, fragment)
                transaction.commit()

                // Verify that R.id.my_self is the correct navigation item ID
                val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
                bottomNav?.selectedItemId = R.id.contacts
            }

        }


    }


}