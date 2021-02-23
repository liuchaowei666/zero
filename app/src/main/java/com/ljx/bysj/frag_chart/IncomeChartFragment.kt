package com.ljx.bysj.frag_chart

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ViewPortHandler
import com.ljx.bysj.R
import com.ljx.bysj.db.DBManage

class IncomeChartFragment : BaseChartFragment() {

    val kind = 1
    var xLabelCount = 0

    override fun onResume() {
        super.onResume()
        loadData(year,month,kind)
    }

    override fun setAxisData(year: Int, month: Int) {
        val sets: MutableList<IBarDataSet> = ArrayList()
        //获取这个月每天的支出总金额
        val list = DBManage.getSumCountOneDayInMonth(year, month, kind)
        if (list.size == 0) {
            barChart.visibility = View.GONE
            chartTv.visibility = View.VISIBLE
        } else {
            barChart.visibility = View.VISIBLE
            chartTv.visibility = View.GONE

            val barEntriesl: MutableList<BarEntry> = ArrayList()
            for (index in 0 until xLabelCount) {
                val entry = BarEntry(index.toFloat(), 0f)
                barEntriesl.add(entry)
            }

            for (i in list.indices) {
                val itemBean = list[i]
                val day = itemBean.day //获取日期
                val xIndex = day - 1
                val barEntry = barEntriesl[xIndex]
                barEntry.y = itemBean.sumcount
            }
            val barDataSet = BarDataSet(barEntriesl, "")
            barDataSet.valueTextColor = Color.BLACK
            barDataSet.valueTextSize = 6f
            barDataSet.color = Color.parseColor("#006400")

            //设置数据显示格式
            barDataSet.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    if (value == 0f){
                        return ""
                    }
                    return value.toString()
                }
            }
            sets.add(barDataSet)

            val barData = BarData(sets)
            barData.barWidth = 0.3f
            barChart.data = barData
        }
    }

    override fun setAxis(year: Int, month: Int) {
        //设置x轴
        barChart.setScaleEnabled(false)
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM //x轴显示在下方
        xAxis.setDrawAxisLine(false)
        xLabelCount = DBManage.getMaxOneDayInMonth(year, month, 1)
        xAxis.labelCount = xLabelCount//设置x轴标签的个数
        xAxis.textSize = 12F  //x轴标签的大小
        xAxis.setDrawGridLines(false)
        //设置x轴显示的格式
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val value = value.toInt()
                if (value == 0) {
                    return "$month-1"
                }
                if (value == xLabelCount - 1){
                    return "$month-$xLabelCount"
                }
                if (xLabelCount > 17){
                    if (value == 14){
                        return "$month-15"
                    }
                }
                return ""
            }


        }
        xAxis.yOffset = 10F

        //y轴在子类的设置
        setYAxis(year,month)
    }

    override fun setYAxis(year: Int, month: Int) {
        //获取本月收入最高的一天支出多少，将它设置为y轴的最大值
        val maxMoney = DBManage.getMaxMoneyOneDayInMonth(year, month, kind)
        //设置y轴
        val yaxisR = barChart.axisRight
        yaxisR.axisMaximum = maxMoney
        yaxisR.axisMinimum = 0f
        yaxisR.isEnabled = false

        val yaxisL = barChart.axisLeft
        yaxisL.axisMaximum = maxMoney
        yaxisL.axisMinimum = 0f
        yaxisL.isEnabled = true

        //设置不显示图例
        val legend = barChart.legend
        legend.isEnabled = false
    }

    override fun setDate(year: Int, month: Int){
        super.setDate(year, month)
        loadData(year, month, kind)
    }
}