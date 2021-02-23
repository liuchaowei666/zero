package com.ljx.bysj.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ljx.bysj.R
import kotlinx.android.synthetic.main.dialog_calendar.view.*

/**
 * 历史账单页面点击日历表，弹出对话框，当中的Gridview对应的适配器
 */
class CalendarAdapter(var context: Context, year: Int): BaseAdapter() {

    private var mDatas:MutableList<String>
    var year: Int = 0
    var selectPos = -1

    @JvmName("setYear1")
    fun setYear(year: Int){
        this.year = year
        mDatas.clear()
        loadDatas(year)
        notifyDataSetChanged()
    }

    init {
        this.year = year
        mDatas = ArrayList()
        loadDatas(year)
    }

    private fun loadDatas(year: Int) {
        for (i in 1 until 13) {
            val data = "$year/$i"
            mDatas.add(data)
        }
    }

    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getItem(position: Int): Any {
        return mDatas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_calendar,parent,false)
        val textView = convertView.findViewById<TextView>(R.id.item_dialog_calendar_tv)
        textView.text = mDatas[position]
        textView.setBackgroundResource(R.color.gray_f3f3f3)
        textView.setTextColor(Color.BLACK)

        if (position == selectPos){
            textView.setBackgroundResource(R.color.blue_2196f7)
            textView.setTextColor(Color.WHITE)
        }

        return convertView
    }
}