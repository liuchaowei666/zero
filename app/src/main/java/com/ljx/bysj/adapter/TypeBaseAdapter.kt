package com.ljx.bysj.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ljx.bysj.R
import com.ljx.bysj.db.TypeBean
import kotlinx.android.synthetic.main.item_recordfrag_gv.view.*

class TypeBaseAdapter() : BaseAdapter() {

    var context: Context? = null
    var mDatas: MutableList<TypeBean>? = null
    var selectPos: Int = 0

    constructor(context: Context, mDatas: MutableList<TypeBean>) : this() {
        this.context = context
        this.mDatas = mDatas

    }

    override fun getCount(): Int {
        return mDatas!!.size
    }

    override fun getItem(position: Int): Any {
        return mDatas!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //此适配器不考虑复用问题，因为所有的Item都显示在界面上，不会因为滑动就消失，所以没有剩余的convertView，所以不用复写
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false)
        //查找布局当中的控件
        val iv: ImageView = convertView.item_recordfrag_iv
        val tv: TextView = convertView.item_recordfrag_tv
        //获取指定位置的数据源
        val typeBean = mDatas!![position]
        tv.text = typeBean.typename
        //判断当前位置是否未选中位置
        if (selectPos == position){
            iv.setImageResource(typeBean.selectimgId)
        }else {
            iv.setImageResource(typeBean.imgId)
        }
        return convertView
    }
}