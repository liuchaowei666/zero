package com.ljx.bysj.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import com.ljx.bysj.R
import com.ljx.bysj.adapter.CalendarAdapter
import com.ljx.bysj.db.DBManage
import kotlinx.android.synthetic.main.dialog_calendar.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarDialog(context: Context,selectPos: Int,selectMonth: Int) : Dialog(context), View.OnClickListener {
    lateinit var errorIv:ImageView;
    lateinit var gv:GridView
    lateinit var hsv:LinearLayout

    private lateinit var hsvViewList:MutableList<TextView>
    private lateinit var yearList: MutableList<Int>

    var selectPos = -1 //表示正在被点击的年份的位置
    lateinit var adapter: CalendarAdapter
    var selectMonth = -1

    interface OnRefreshListener{
        fun OnRefresh(selectPos: Int,year: Int,month: Int)
    }
    lateinit var onRefreshListener:OnRefreshListener

    fun setOnRefeshListener(onRefreshListener: OnRefreshListener){
        this.onRefreshListener = onRefreshListener
    }

    init {
        this.selectPos = selectPos
        this.selectMonth = selectMonth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_calendar)
        errorIv = dialog_calendar_iv
        gv = dialog_calendar_gv
        hsv = dialog_calendar_layout
        errorIv.setOnClickListener(this)

        //向横向的的scrollview当中添加View的方法
        addViewToLayout()
        initGridView()
        //设置Gridview当中每一个item的点击事件
        setGvListener()
    }

    private fun setGvListener() {
        gv.setOnItemClickListener { parent, view, position, id ->
            adapter.selectPos = position
            adapter.notifyDataSetChanged()
            val month = position + 1
            val year = adapter.year
            //获取被选中的年份和月份
            onRefreshListener.OnRefresh(selectPos,year,month)
            cancel()
        }
    }

    private fun initGridView() {
        val selectYear = yearList[selectPos]
        adapter = CalendarAdapter(context, selectYear)
        if (selectMonth == -1) {
            val month = Calendar.getInstance().get(Calendar.MONTH)
            adapter.selectPos = month
        }else {
            adapter.selectPos = selectMonth - 1
        }
        gv.adapter = adapter
    }

    private fun addViewToLayout() {
        hsvViewList = ArrayList() //将添加进入线性布局当中的Textview进行统一管理的集合
        yearList = DBManage.getYearListFromAccounttb()

//        如果数据库当中没有记录，就添加今年的记录
        if (yearList.size == 0) {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            yearList.add(year)
        }
//        遍历年份，有几年，就向Scrollview中添加几个view
        for (i in yearList.indices){
            val year = yearList[i]
            val view = layoutInflater.inflate(R.layout.item_dialog_calendar_hsc, null)
            hsv.addView(view)
            val hsvTv = view.findViewById<TextView>(R.id.item_dialog_calendar_hsv_tv)
            hsvTv.text = "$year"
            hsvViewList.add(hsvTv)
        }

        if (selectPos == -1){
            selectPos = hsvViewList.size - 1 //设置当前被选中的是最近的年份
        }
        changeTvBg(selectPos)
        setHsvClickListener()
    }

    /**
     * 给横向的scrollview当中每一个textview设置点击事件
     */
    private fun setHsvClickListener() {
        for (i in hsvViewList.indices){
            val textview = hsvViewList[i]
            textview.setOnClickListener {
                changeTvBg(i)
                selectPos = i
                //获取被选中的年份，然后下面的Gridview显示数据源会发生变化
                val year = yearList[selectPos]
                adapter.setYear(year)
            }
        }
    }

    /**
     * 传入被选中的位置，改变此位置上的背景和文字颜色
     */
    private fun changeTvBg(selectPos: Int) {
        for (i in hsvViewList.indices){
            val tv = hsvViewList[i]
            tv.setBackgroundResource(R.drawable.dialog_btn_cancel_bg)
            tv.setTextColor(Color.parseColor("#2196f7"))
        }

        val selectTv = hsvViewList[selectPos]
        selectTv.setBackgroundResource(R.drawable.dialog_btn_ensure_bg)
        selectTv.setTextColor(Color.WHITE)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.dialog_calendar_iv -> {
                cancel()
            }
            else -> {
            }
        }
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
        wlp.gravity = Gravity.TOP
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.attributes = wlp
    }
}