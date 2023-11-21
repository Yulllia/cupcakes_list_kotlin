package com.example.cupcakeslist.dessertsList

import com.example.cupcakeslist.R
import com.example.cupcakeslist.dessertDetail.CupcakesDetailFragment
import com.example.cupcakeslist.enumData.DessertOption


class CupcakesFragment() : DessertListAbstract() {
    override fun getLayoutResource(): Int {
        return  R.layout.fragment_cupcakes
    }
    override fun getListDessert(): Int {
        return  R.id.listCupcakes
    }

    override var dessertSelection: String = DessertOption.CUPCAKES.displayName

    override fun dessertDetailFragment(position: Int): CupcakesDetailFragment {
        return  CupcakesDetailFragment.newInstance(position)
    }
}



