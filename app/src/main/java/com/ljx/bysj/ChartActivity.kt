package com.ljx.bysj

import android.graphics.Color
import android.icu.math.BigDecimal
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ljx.bysj.adapter.ChartVpAdapter
import com.ljx.bysj.db.DBManage
import com.ljx.bysj.frag_chart.BaseChartFragment
import com.ljx.bysj.frag_chart.ExpendChartFragment
import com.ljx.bysj.frag_chart.IncomeChartFragment
import com.ljx.bysj.utils.CalendarDialog
import kotlinx.android.synthetic.main.activity_chart.*
import kotlin.time.days

class ChartActivity : AppCompatActivity() {
    lateinit var expendBtn: Button
    lateinit var incomeBtn: Button
    lateinit var dateTv: TextView
    lateinit var expendTv: TextView
    lateinit var incomeTv: TextView
    lateinit var chartVp: ViewPager2
    var year = 0
    var month = 0
    var day = 0
    var dialogselectPos = -1
    var dialogselectMonth = -1
    lateinit var chartFragmentlist: MutableList<Fragment>
    var expendChartFragment = ExpendChartFragment()
    var incomeChartFragment = IncomeChartFragment()
    lateinit var chartVpAdapter:ChartVpAdapter



    private val scale = 2 //保留两位小数
    private val roundingMode = 4 //四舍五入
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        initView()
        initTime()
        initDetailData(year,month)
        initFragement()
        setVpScrollListener()
    }

    private fun setVpScrollListener() {
        chartVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeBtnStyle(position)
            }
        })
    }

    private fun initFragement() {
        chartFragmentlist = ArrayList()
//        添加Fragement对象
        expendChartFragment = ExpendChartFragment()
        incomeChartFragment = IncomeChartFragment()
//        添加数据到Fragement
        val bundle = Bundle()
        bundle.putInt("year",year)
        bundle.putInt("month",month)
        expendChartFragment.arguments = bundle
        incomeChartFragment.arguments = bundle
//        将Fragement添加到数据源
        chartFragmentlist.add(expendChartFragment)
        chartFragmentlist.add(incomeChartFragment)
        //使用适配器
        chartVpAdapter = ChartVpAdapter(this, chartFragmentlist)
        chartVp.adapter = chartVpAdapter
        chartVp.offscreenPageLimit = 2
        //将Fragement加载到Activity中
    }

    /**
     * 统计收支情况
     */
    private fun initDetailData(year: Int, month: Int) {
        val calendar = Calendar.getInstance()
        day = if (month == calendar[Calendar.MONTH] + 1){
            calendar[Calendar.DAY_OF_MONTH]
        }else {
            calendar.set(year,month,0)
            calendar[Calendar.DAY_OF_MONTH]
        }
        val averageExpend = DBManage.getSumMoneyOneMonth(year, month, 0) / day
        val averageIncome = DBManage.getSumMoneyOneMonth(year, month, 1) / day
        val expendCount = DBManage.getCountItemOneMonth(year, month, 0)
        val incomeCount = DBManage.getCountItemOneMonth(year, month, 1)
        dateTv.text = "${year}年${month}月"
        //保留两位小数并四舍五入
        var expendbigDecimal = BigDecimal(-averageExpend.toDouble())
        expendbigDecimal = expendbigDecimal.setScale(scale,roundingMode)
        var incomebigDecimal = BigDecimal(averageIncome.toDouble())
        incomebigDecimal = incomebigDecimal.setScale(scale,roundingMode)
        expendTv.text = expendbigDecimal.toString()
        incomeTv.text = incomebigDecimal.toString()
    }

    /**
     * 初始化时间
     */
    private fun initTime() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH] + 1
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        expendBtn = chart_btn_expend
        incomeBtn = chart_btn_income
        dateTv = chart_tv_date
        expendTv = chart_tv_expend
        incomeTv = chart_tv_income
        chartVp = chart_vp
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.chart_iv_back -> {
                finish()
            }
            R.id.chart_iv_calander -> {
                showCalendarDialog()
            }
            R.id.chart_btn_expend -> {
                changeBtnStyle(0)
                chartVp.currentItem = 0
            }
            R.id.chart_btn_income -> {
                changeBtnStyle(1)
                chartVp.currentItem = 1
            }
        }
    }

    //显示日历对话框
    private fun showCalendarDialog() {
        val dialog = CalendarDialog(this, dialogselectPos, dialogselectMonth)
        dialog.show()
        dialog.setDialogSize()
        dialog.setOnRefeshListener(object : CalendarDialog.OnRefreshListener{
            override fun OnRefresh(selectPos: Int, year: Int, month: Int) {
                ChartActivity().dialogselectPos = selectPos
                ChartActivity().dialogselectMonth = month
                initDetailData(year,month)
                expendChartFragment.setDate(year, month)
                incomeChartFragment.setDate(year, month)

                dialogselectPos = selectPos
                dialogselectMonth = month
            }

        })
    }

    /**
     * 设置按钮样式的改变  支出=0 收入=1
     */
    private fun changeBtnStyle(kind: Int) {
        if (kind == 0) {
            expendBtn.setBackgroundResource(R.drawable.dialog_btn_ensure_bg)
            expendBtn.setTextColor(Color.WHITE)
            incomeBtn.setBackgroundResource(R.drawable.dialog_btn_cancel_bg)
            incomeBtn.setTextColor(R.color.blue_2196f7)
        }else {
            incomeBtn.setBackgroundResource(R.drawable.dialog_btn_ensure_bg)
            incomeBtn.setTextColor(Color.WHITE)
            expendBtn.setBackgroundResource(R.drawable.dialog_btn_cancel_bg)
            expendBtn.setTextColor(R.color.blue_2196f7)
        }
    }
}