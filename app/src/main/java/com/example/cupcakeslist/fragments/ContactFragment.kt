package com.example.cupcakeslist.fragments

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.cupcakeslist.R


class ContactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.textView)

        val htmlText =
            "<html><body>" +
                    "<p style=\"margin-top: 16px;\">Контактний номер: <span style=\"color:#c803da; text-decoration: underline; margin-top: 16px;\"><a href=\"tel:+380964735817\">+380964735817</a></span></p>" +
                    "<p style=\"margin-top: 16px;\">Telegram: <span style=\"color:#c803da; text-decoration: underline; margin-top: 16px;\"> <a href=\"https://t.me/YuliaShyshka\">Yulia Kruk</a></span></p>" +
                    "<p style=\"margin-top: 16px;\">Email: <span style=\"color:#c803da; text-decoration: underline; margin-top: 16px;\"><a href=\"mailto:shyshka20@gmail.com\">shyshka20@gmail.com</a></span></p>" +
                    "<p>Адреса: місто Львів, вул. Очаківська 7</p>" +
                    "<p>Ми в «Інстаграм»:  <span style=\"color:#c803da; text-decoration: underline; margin-top: 16px;\"> <a href=\"https://www.instagram.com/yuliia_cupcakes/\">Instagram</a></span></p>" +
                    "</body></html>"

        textView.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}