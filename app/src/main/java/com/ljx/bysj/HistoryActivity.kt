package com.ljx.bysj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.ljx.bysj.adapter.AccountAdapter
import com.ljx.bysj.db.AccountBean
import com.ljx.bysj.db.DBManage
import com.ljx.bysj.utils.CalendarDialog
import com.ljx.bysj.utils.DetailDialog
import kotlinx.android.synthetic.main.activity_history.*
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {
    private lateinit var historyiv:ListView
    lateinit var timeTv:TextView
    lateinit var mDatas:MutableList<AccountBean>
    lateinit var adapter:AccountAdapter
    var year:Int = 0
    var month:Int = 0

    var dialogSelPos = -1
    var dialogSelMonth = -1

    var topExpendTv: TextView? = null
    var topIncomeTv: TextView? = null
    var topSurplusTv: TextView? = null
    var topConTv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        historyiv = history_lv
        timeTv = history_tv_time
        mDatas = ArrayList()
        //设置适配器
        adapter = AccountAdapter(this,mDatas)
        historyiv.adapter = adapter
        initTime()
        timeTv.text = "${year}年${month}月"
        loadData(year,month)
        setLvClickListener()
    }

    private fun setLvClickListener() {
        historyiv.setOnItemLongClickListener { parent, view, position, id ->
            val accountBean = mDatas[position]
            showDetailDialog(accountBean, mDatas, adapter)
            false
        }
    }

    private fun showDetailDialog(selectBean: AccountBean, mDatas: MutableList<AccountBean>?, adapter: AccountAdapter?) {
        val dialog = DetailDialog(this)
        dialog.show()
        dialog.setDialogSize()
        dialog.getSelectBean(selectBean, mDatas!!, adapter)
        dialog.setOnDeleteListener(object : DetailDialog.OnDeleteListener{
            override fun onDelete(info:String,Income:Float,Expend:Float) {
            }

        })
    }

    private fun loadData(year: Int,month: Int) {
        val list = DBManage.getAListOneMonthFromAtb(year, month)
        mDatas.clear()
        mDatas.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun initTime() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH] + 1

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.history_iv_back -> {
                finish()
            }
            R.id.history_iv_calander -> {
                val calendarDialog = CalendarDialog(this,this.dialogSelPos,this.dialogSelMonth)
                calendarDialog.show()
                calendarDialog.setDialogSize()
                calendarDialog.setOnRefeshListener(object : CalendarDialog.OnRefreshListener{
                    override fun OnRefresh(selectPos: Int, year: Int, month: Int) {
                        timeTv.text = year.toString() + "年" + month.toString() + "月"
                        loadData(year, month)
                        dialogSelPos = selectPos
                        dialogSelMonth = month
                    }

                })
            }
        }
    }

    fun afterDelete(selectBean: AccountBean, selectmDatas: MutableList<AccountBean>, selectAdapter: AccountAdapter) {
        selectmDatas.remove(selectBean)
        mDatas = selectmDatas
        selectAdapter.notifyDataSetChanged()
        adapter = selectAdapter
    }
}