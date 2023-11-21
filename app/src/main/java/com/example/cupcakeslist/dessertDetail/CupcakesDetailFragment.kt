package com.example.cupcakeslist.dessertDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cupcakeslist.R
import com.example.cupcakeslist.dessertsList.CupcakesFragment
import com.example.cupcakeslist.enumData.DessertOption


class CupcakesDetailFragment: DetailFragment() {

    override fun getFragmentLayoutId(): Int {
        return R.layout.fragment_detail_desserts
    }

    override fun getRecyclerViewId(): Int {
        return R.id.listDessertsDetail
    }

    override fun getDessertType(): String {
        return DessertOption.CUPCAKES.displayName
    }

    override fun getFragment(): Fragment {
        return CupcakesFragment()
    }

    companion object {
        fun newInstance(position: Int): CupcakesDetailFragment {
            val fragment = CupcakesDetailFragment()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }
}