package com.ljx.bysj.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ljx.bysj.R
import com.ljx.bysj.db.ChartItemBean
import com.ljx.bysj.utils.FloatUtils
import kotlinx.android.synthetic.main.item_chart_frag_lv.view.*
import kotlinx.android.synthetic.main.item_mainlv.view.*

/**
 * 账单详情页面Listview的适配器
 */
class ChartItemAdapter(var context: Context,var mDatas: MutableList<ChartItemBean>): BaseAdapter() {

    var inflater: LayoutInflater? = null

    init {
        inflater = LayoutInflater.from(context)
    }
    override fun getCount(): Int {
        return  mDatas.size
    }

    override fun getItem(position: Int): Any {
        return mDatas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val holder: ViewHolder
        if (convertView == null){
            convertView = inflater!!.inflate(R.layout.item_chart_frag_lv,parent,false)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        }else {
            holder = convertView.tag as ViewHolder
        }
        //获取显示内容
        val bean = mDatas[position]
        holder.typeIv.setImageResource(bean.sImageId)
        holder.typeTv.text = bean.type
        holder.totalTv.text = bean.totalCount.toString()
        val ratio = bean.ratio
        val per = FloatUtils().ratioToPercent(ratio)

        holder.ratioTv.text = per
        return convertView
    }

    internal inner class ViewHolder(view: View) {
        var typeIv = view.item_chartfrag_iv!!
        var typeTv = view.item_chartfrag_tv_kind!!
        var totalTv = view.item_chartfrag_tv_count!!
        var ratioTv = view.item_chartfrag_tv_ratio!!

    }
}