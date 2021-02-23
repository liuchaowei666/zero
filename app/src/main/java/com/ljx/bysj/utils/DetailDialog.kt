package com.ljx.bysj.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.ljx.bysj.HistoryActivity
import com.ljx.bysj.MainActivity
import com.ljx.bysj.R
import com.ljx.bysj.RecordActivity
import com.ljx.bysj.adapter.AccountAdapter
import com.ljx.bysj.db.AccountBean
import com.ljx.bysj.db.DBManage
import kotlinx.android.synthetic.main.dialog_detail.*
import kotlinx.android.synthetic.main.item_mainlv_top.*
import java.util.*

class DetailDialog(context: Context) : Dialog(context), View.OnClickListener{

    var sort: TextView? = null
    var count: TextView? = null
    var deleteBtn: Button? =null
    var modifyBtn: Button? =null
    var selectBean: AccountBean? = null
    var selectmDatas: MutableList<AccountBean>? = null
    var selectAdapter: AccountAdapter? = null

    interface OnDeleteListener{
        fun onDelete(info:String,Income:Float,Expend:Float)
    }
    private var onDeleteListener: OnDeleteListener? =null

    fun setOnDeleteListener(onDeleteListener: OnDeleteListener){
        this.onDeleteListener = onDeleteListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_detail)//设置对话框显示布局
        sort = dialog_detail_sort_tv2
        count = dialog_detail_count_tv2
        deleteBtn = dialog_detail_btn_delete
        modifyBtn = dialog_detail_btn_modify
        deleteBtn!!.setOnClickListener(this)
        modifyBtn!!.setOnClickListener(this)
    }



    fun getSelectBean(bean: AccountBean, mDatas: MutableList<AccountBean>, adapter: AccountAdapter?){
        selectBean = bean
        selectmDatas = mDatas
        selectAdapter = adapter
        sort!!.text = bean.typename
        count!!.text = bean.count.toString()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.dialog_detail_btn_delete -> {
                deleteItem(selectBean!!,selectmDatas!!,selectAdapter!!)
                dismiss()
            }
            R.id.dialog_detail_btn_modify -> {
                val intent = Intent()
                intent.setClass(v.context,RecordActivity().javaClass)
                context.startActivity(intent)
            }
        }
    }



    //弹出是否删除记录的对话框
    private fun deleteItem(selectBean: AccountBean, selectmDatas: MutableList<AccountBean>, selectAdapter: AccountAdapter) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("删除").setMessage("您确定要删除这条记录吗？")
            .setNegativeButton("取消",null)
            .setPositiveButton("确定") { dialog, which -> //执行删除操作
                var selectId = selectBean.id
                DBManage.deleteItemFromAccounttbById(selectId)
                selectmDatas.remove(selectBean)
                selectAdapter.notifyDataSetChanged()
                MainActivity().afterDelete(selectBean, selectmDatas, selectAdapter)
                HistoryActivity().afterDelete(selectBean, selectmDatas, selectAdapter)
                val calendar = Calendar.getInstance()
                val year = calendar[Calendar.YEAR]
                val month = calendar[Calendar.MONTH] + 1
                val day = calendar[Calendar.DAY_OF_MONTH]
                //获取今日支出和收入总金额，显示在View中
                var IncomeOneDay = DBManage.getSumMoneyOneDay(year, month, day, 1)
                var ExpendOneDay = DBManage.getSumMoneyOneDay(year, month, day, 0)
                var infoOneDay = "今日支出 $ExpendOneDay 收入 $IncomeOneDay"
                //获取本月支出和收入总金额，显示在View中
                var IncomeOneMonth = DBManage.getSumMoneyOneMonth(year, month, 1)
                var ExpendOneMonth = DBManage.getSumMoneyOneMonth(year, month, 0)
                onDeleteListener!!.onDelete(infoOneDay, IncomeOneMonth, ExpendOneMonth)
            }
        builder.create().show()  //显示对话框
    }


    //设置Dialog的尺寸和屏幕尺寸一致
    fun setDialogSize(){
        //获取当前窗口对象
        val window = window
        //获取窗口对象的参数
        val wlp = window!!.attributes
        //获取屏幕宽度
        val display = window.windowManager.defaultDisplay
        wlp.width = display.width  //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.attributes = wlp
    }
}