package com.ljx.bysj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ljx.bysj.adapter.RecordPagerAdapter
import com.ljx.bysj.frag_record.BaseRecordFragment
import com.ljx.bysj.frag_record.ExpendFragment
import com.ljx.bysj.frag_record.IncomeFragment
import kotlinx.android.synthetic.main.activity_record.*
import java.util.ArrayList

class RecordActivity : AppCompatActivity() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        //1.查找控件
        tabLayout = record_tabs
        viewPager = record_vp
        //2.设置ViewPager加载页面
        initPager()
    }

    private fun initPager(){
        //初始化ViewPager页面的集合
        val fragmentlist : MutableList<Fragment> = ArrayList()
        //创建收入和支出页面，放置在Fragment当中
        val expFrg = ExpendFragment() //支出
        val incFrg = IncomeFragment() //收入
        fragmentlist.add(expFrg)
        fragmentlist.add(incFrg)


        //设置适配器对象
        val pagerAdapter = RecordPagerAdapter(supportFragmentManager, fragmentlist)
        //        设置适配器
        viewPager!!.adapter = pagerAdapter
        //将TabLayout和ViwePager进行关联
        tabLayout!!.setupWithViewPager(viewPager)
    }

//    点击事件
    fun onClick(view: View) {
        when(view.id){
            R.id.record_iv_back ->
                finish()
        }
    }
}