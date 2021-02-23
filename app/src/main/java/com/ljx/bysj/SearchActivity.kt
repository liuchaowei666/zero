package com.ljx.bysj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import com.ljx.bysj.adapter.AccountAdapter
import com.ljx.bysj.db.AccountBean
import com.ljx.bysj.db.DBManage
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private lateinit var searchLv: ListView
    private lateinit var searchEt: EditText
    private lateinit var mDatas: MutableList<AccountBean>
    private lateinit var adapter: AccountAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        mDatas = ArrayList()
        adapter = AccountAdapter(this,mDatas)
        searchLv.adapter = adapter
    }

    private fun initView() {
        searchLv = search_lv
        searchEt = search_et
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.search_iv_back ->{
                finish()
            }
            R.id.search_iv_search -> { //搜索
                val msg = searchEt.text.toString().trim()
                //开始搜索
                val list = DBManage.getAccountListByRemarkFromAccounttb(msg)
                mDatas.clear()
                mDatas.addAll(list)
                adapter.notifyDataSetChanged()
            }
        }
    }
}