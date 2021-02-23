package com.ljx.bysj.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import com.ljx.bysj.*
import kotlinx.android.synthetic.main.dialog_more.*

class MoreDialog(context: Context) : Dialog(context), View.OnClickListener{
    lateinit var aboutBtn: Button
    lateinit var settingBtn:Button
    lateinit var historyBtn:Button
    lateinit var infoBtn:Button
    lateinit var registBtn: Button
    lateinit var errorIv:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_more)
        aboutBtn = dialog_more_about
        settingBtn = dialog_more_setting
        historyBtn = dialog_more_record
        infoBtn = dialog_more_accountinfo
        errorIv = dialog_more_iv
        registBtn = dialog_more_regist

        aboutBtn.setOnClickListener(this)
        settingBtn.setOnClickListener(this)
        historyBtn.setOnClickListener(this)
        infoBtn.setOnClickListener(this)
        errorIv.setOnClickListener(this)
        registBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        when (v!!.id){
            R.id.dialog_more_about -> {
                intent.setClass(context,AboutActivity().javaClass)
                context.startActivity(intent)
            }
            R.id.dialog_more_setting -> {

            }
            R.id.dialog_more_record -> {
                intent.setClass(context,HistoryActivity().javaClass)
                context.startActivity(intent)
            }
            R.id.dialog_more_accountinfo -> {

            }
            R.id.dialog_more_iv -> {

            }
            R.id.dialog_more_regist -> {
                intent.setClass(context,RegistActivity().javaClass)
                context.startActivity(intent)
            }
        }
        cancel()
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