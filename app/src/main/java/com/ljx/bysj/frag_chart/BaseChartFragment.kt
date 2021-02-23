package com.ljx.bysj.frag_chart

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.ljx.bysj.R
import com.ljx.bysj.adapter.ChartItemAdapter
import com.ljx.bysj.db.ChartItemBean
import com.ljx.bysj.db.DBManage
import kotlinx.android.synthetic.main.item_chartfrag_top.*

abstract class BaseChartFragment : Fragment() {

    lateinit var chartLv: ListView
    lateinit var mDatas: MutableList<ChartItemBean>
    var year = 0
    var month = 0
    lateinit var itemAdapter: ChartItemAdapter

    lateinit var barChart: BarChart//代表柱状图的控件
    lateinit var chartTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expend_chart, container, false)
        chartLv = view.findViewById(R.id.frag_chart_lv)
        //获取Activity传递的数据
        val bundle = arguments
        year = bundle!!.getInt("year")
        month = bundle.getInt("month")
        //设置数据源
        mDatas = ArrayList()
        //设置适配器
        itemAdapter = ChartItemAdapter(context!!, mDatas)
        chartLv.adapter = itemAdapter
        //添加头布局
        addLvHeaderView()
        return view
    }

    private fun addLvHeaderView() {
        //将布局转换成View对象
        val headerView = layoutInflater.inflate(R.layout.item_chartfrag_top, null)
        //将View添加到ListView的头布局
        chartLv.addHeaderView(headerView)
        //查找头布局包含的控件
        barChart = headerView.findViewById(R.id.item_chartfrag_top_chart)
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv)
        //设定柱状图不显示描述信息
        barChart.description.isEnabled = false
        //设置柱状图的内边距
        barChart.setExtraOffsets(10F, 10F, 10F, 10F)
        setAxis(year, month)
        //设置坐标轴显示的数据
        setAxisData(year,month)
    }


    /**
     * 设置坐标轴显示的数据
     */
    abstract fun setAxisData(year: Int, month: Int)

    //设置柱状图坐标轴的显示  方法必须重写
    abstract fun setAxis(year: Int, month: Int)

    //设置y轴
    protected abstract fun setYAxis(year: Int,month: Int)

    open fun setDate(year: Int, month: Int) {
        this.year = year
        this.month = month
        barChart.clear()
        barChart.invalidate()
        setAxis(year, month)
        setAxisData(year, month)
    }

    fun loadData(year: Int, month: Int, kind: Int) {
        val list = DBManage.getChartListFromAccounttb(year, month, kind)
        mDatas.clear()
        mDatas.addAll(list)
        itemAdapter.notifyDataSetChanged()
    }
}
