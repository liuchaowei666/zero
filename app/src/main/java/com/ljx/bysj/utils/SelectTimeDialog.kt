package com.ljx.bysj.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import com.ljx.bysj.R
import kotlinx.android.synthetic.main.dialog_time.*

/**
 * 在记录页面弹出时间对话框
 */
class SelectTimeDialog(context: Context) : Dialog(context), View.OnClickListener{

    var hourEt:EditText? = null
    var minuteEt: EditText? = null
    var datePicker:DatePicker? = null
    var ensureBtn: Button? = null
    var cancelBtn: Button? =null

    interface OnEnsureListener{
        fun onEnsure(time: String,year: Int,month: Int,day: Int)
    }
    private var onEnsureListener: OnEnsureListener? =null

    fun setOnEnsureListener(onEnsureListener: OnEnsureListener){
        this.onEnsureListener = onEnsureListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_time)
        hourEt = dialog_time_et_hour
        minuteEt = dialog_time_et_minute
        datePicker = dialog_time_dp
        ensureBtn = dialog_time_btn_ensure
        cancelBtn = dialog_time_btn_cancel

        ensureBtn!!.setOnClickListener(this)
        cancelBtn!!.setOnClickListener(this)

        hideDatePickerHeader()

    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.dialog_time_btn_cancel -> {
                cancel()
            }
            R.id.dialog_time_btn_ensure -> {
                val year = datePicker!!.year
                val month = datePicker!!.month + 1
                val dayOfMonth = datePicker!!.dayOfMonth
                var monthStr = month.toString()
                if (month < 10){
                    monthStr = "0$monthStr"
                }
                var dayStr = dayOfMonth.toString()
                if (dayOfMonth < 10){
                    dayStr = "0$dayStr"
                }

                //获取输入的时和分
                var hourStr = hourEt!!.text.toString()
                var minuteStr = minuteEt!!.text.toString()
                var hour = 0
                if (!TextUtils.isEmpty(hourStr)){
                    hour = hourStr.toInt()
                    hour %= 24
                }
                var minute = 0
                if (!TextUtils.isEmpty(minuteStr)){
                    minute = minuteStr.toInt()
                    minute %= 24
                }

                hourStr = hour.toString()
                minuteStr = minute.toString()
                if (hour < 10){
                    hourStr = "0$hourStr"
                }
                if (minute < 10){
                    minuteStr = "0$minuteStr"
                }

                val timeFormat: String = "$year"+"年"+monthStr+"月"+dayStr+"日 "+hourStr+" : "+minuteStr
                onEnsureListener!!.onEnsure(timeFormat,year,month,dayOfMonth)
                cancel()
            }
        }
    }

    //隐藏DatePicker头布局
    private fun hideDatePickerHeader(){
        val rootview:ViewGroup = datePicker!!.getChildAt(0) as ViewGroup
        val headerView = rootview.getChildAt(0)

        //5.0+
        var headerId = context.resources.getIdentifier("day_picker_selector_layout", "id", "android")
        if (headerId == headerView.id){
            headerView.visibility = View.GONE
            val layoutParamsRoot = rootview.layoutParams
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT
            rootview.layoutParams = layoutParamsRoot

            val animator: ViewGroup = rootview.getChildAt(1) as ViewGroup
            val layoutParamsAnimator = animator.layoutParams
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT
            animator.layoutParams = layoutParamsAnimator

            val child: ViewGroup = animator.getChildAt(0) as ViewGroup
            val layoutParamsChild = animator.layoutParams
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT
            child.layoutParams = layoutParamsChild
        }

        //6.0+
        headerId = context.resources.getIdentifier("date_picker_header","id","android")
        if (headerId == headerView.id) {
            headerView.visibility = View.GONE
        }
    }
}