package com.example.cupcakeslist.dessertsList

import com.example.cupcakeslist.R
import com.example.cupcakeslist.dessertDetail.MacaroonsDetailFragment
import com.example.cupcakeslist.enumData.DessertOption


class MacaroonsFragment(): DessertListAbstract() {
    override fun getLayoutResource(): Int {
        return  R.layout.fragment_macarons
    }
    override fun getListDessert(): Int {
        return  R.id.listMacaroons
    }
    override var dessertSelection: String = DessertOption.MACAROONS.displayName

    override fun dessertDetailFragment(position: Int): MacaroonsDetailFragment {
        return  MacaroonsDetailFragment.newInstance(position)
    }
}