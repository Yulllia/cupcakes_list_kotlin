package com.example.cupcakeslist.dessertDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cupcakeslist.R
import com.example.cupcakeslist.dessertsList.MacaroonsFragment
import com.example.cupcakeslist.enumData.DessertOption

class MacaroonsDetailFragment: DetailFragment() {

    override fun getFragment(): Fragment {
        return MacaroonsFragment()
    }

    override fun getFragmentLayoutId(): Int {
        return R.layout.fragment_detail_desserts
    }

    override fun getRecyclerViewId(): Int {
        return R.id.listDessertsDetail
    }

    override fun getDessertType(): String {
        return DessertOption.MACAROONS.displayName
    }
    companion object {
        fun newInstance(position: Int): MacaroonsDetailFragment {
            val fragment = MacaroonsDetailFragment()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }
}