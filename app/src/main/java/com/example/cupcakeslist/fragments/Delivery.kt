package com.example.cupcakeslist.fragments

import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cupcakeslist.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class Delivery:  Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pays, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spannableStringBuilder =
            SpannableStringBuilder(Html.fromHtml(getString(R.string.howContact)))
        val textView = view.findViewById<TextView>(R.id.howContact)
        textView.movementMethod = LinkMovementMethod.getInstance()

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Verify that fragmentManager is not null
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

            override fun updateDrawState(ds: TextPaint) {
                // Style the clickable text
                ds.color = ContextCompat.getColor(context!!, R.color.colorPrimary)
                ds.isUnderlineText = true // Add underline to make it look like a link
            }
        }

        val linkText = "Контакти"
        val start = spannableStringBuilder.indexOf(linkText)
        val end = start + linkText.length

        spannableStringBuilder.setSpan(
            clickableSpan,
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableStringBuilder

    }

}