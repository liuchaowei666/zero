package com.ljx.bysj.adapter

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class RecordPagerAdapter(fm: FragmentManager, fragmentList: List<Fragment>) :
    FragmentPagerAdapter(fm) {
    var fragmentList: List<Fragment> = fragmentList
    var titles = arrayOf("支出", "收入")
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

}