package com.ljx.bysj.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.ljx.bysj.R
import kotlinx.android.synthetic.main.dialog_remark.*

class RemarkDialog(context: Context) : Dialog(context), View.OnClickListener{

    private var et: EditText? = null
    var cancelBtn: Button? =null
    var ensureBtn: Button? =null

    private var onEnsureListener: KeyBoardUtils.OnEnsureListener? = null
    fun setOnEnsureListener(onEnsureListener: KeyBoardUtils.OnEnsureListener){
        this.onEnsureListener = onEnsureListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_remark)//设置对话框显示布局
        et = dialog_remark_et
        cancelBtn = dialog_remark_btn_cancel
        ensureBtn = dialog_remark_btn_ensure
        cancelBtn!!.setOnClickListener(this)
        ensureBtn!!.setOnClickListener(this)
    }

    interface OnEnsureListener {
        fun onEnsure()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.dialog_remark_btn_cancel ->
                cancel()
            R.id.dialog_remark_btn_ensure ->
                onEnsureListener!!.onEnsure()
        }
    }

    //获取输入数据的方法
    fun getEditText(): String{
        return et!!.text.toString().trim()
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
        handler.sendEmptyMessageDelayed(1,100)
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            //自动弹出软键盘
            val inputMethodManager: InputMethodManager = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}