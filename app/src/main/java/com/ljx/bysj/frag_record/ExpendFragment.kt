package com.ljx.bysj.frag_record

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ljx.bysj.R
import com.ljx.bysj.db.AccountBean
import com.ljx.bysj.db.DBManage

/**
 * A simple [Fragment] subclass.
 */
class ExpendFragment : BaseRecordFragment() {


    //重写
    override fun loadDataToGv(){
        super.loadDataToGv()
        //获取数据库中的数据源
        val expendlist = DBManage.getTypeList(0)
        typeList!!.addAll(expendlist)
        adapter!!.notifyDataSetChanged()
        typeTv!!.text = "三餐"
        typeIv!!.setImageResource(R.mipmap.ic_canyin_fs)
        loadDefault()
    }

    private fun loadDefault() {
        accountBean!!.typename = "三餐"
        accountBean!!.selectimgId = R.mipmap.ic_canyin_fs
    }

    override fun saveAccountToDB() {
        accountBean!!.kind = 0
        DBManage.insertItemToAccounttb(accountBean!!)
    }

}