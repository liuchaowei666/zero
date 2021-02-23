package com.ljx.bysj.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChartVpAdapter(fa: FragmentActivity,fragementList: MutableList<Fragment>) : FragmentStateAdapter(fa) {
    private var fragementList: MutableList<Fragment>
    init {
        this.fragementList = fragementList
    }

    override fun getItemCount(): Int {
        return fragementList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragementList[position]
    }

}