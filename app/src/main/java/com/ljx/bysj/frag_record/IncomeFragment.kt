package com.ljx.bysj.frag_record

import androidx.fragment.app.Fragment
import com.ljx.bysj.R
import com.ljx.bysj.db.DBManage

/**
 * A simple [Fragment] subclass.
 */
class IncomeFragment : BaseRecordFragment() {

    //重写
    override fun loadDataToGv(){
        super.loadDataToGv()
        //获取数据库中的数据源
        val incomelist = DBManage.getTypeList(1)
        typeList!!.addAll(incomelist)
        adapter!!.notifyDataSetChanged()
        typeTv!!.text = "工资"
        typeIv!!.setImageResource(R.mipmap.in_gongzi_fs)
        loadDefault()
    }

    private fun loadDefault() {
        accountBean!!.typename = "工资"
        accountBean!!.selectimgId = R.mipmap.in_gongzi_fs
    }

    override fun saveAccountToDB() {
        accountBean!!.kind = 1
        DBManage.insertItemToAccounttb(accountBean!!)
    }
}