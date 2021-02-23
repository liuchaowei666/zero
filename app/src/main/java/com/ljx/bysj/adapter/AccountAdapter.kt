package com.ljx.bysj.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ljx.bysj.R
import com.ljx.bysj.db.AccountBean
import kotlinx.android.synthetic.main.item_mainlv.view.*
import java.util.*

class AccountAdapter(var context: Context,var mDatas: List<AccountBean>): BaseAdapter() {

    var inflater:LayoutInflater? = null
    var year = 0
    var month = 0
    var day = 0

    init {
        inflater = LayoutInflater.from(context)
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
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
        var convertView: View? = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            convertView = inflater!!.inflate(R.layout.item_mainlv, parent, false)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val bean = mDatas[position]
        holder.typeIv.setImageResource(bean.selectimgId)
        holder.typeTv.text = bean.typename
        holder.remarkTv.text = bean.remark
        holder.timeTv.text = bean.time
        holder.countTv.text = bean.count.toString()
        if ((bean.year == year && bean.month == month && bean.day == day)) {
            val time = bean.time!!.split("")[1]
            holder.timeTv.text = "今天 $time"
        }else{
            holder.timeTv.text = bean.time
        }
        holder.countTv.text = bean.count.toString()
        return convertView


    }

    internal inner class ViewHolder(view: View) {
        var typeIv: ImageView = view.item_mainlv_iv
        var typeTv: TextView = view.item_mainlv_tv_title
        var remarkTv: TextView = view.item_mainlv_tv_remark
        var timeTv: TextView = view.item_mainlv_tv_time
        var countTv: TextView = view.item_mainlv_tv_count
    }
}