package com.ljx.bysj.utils

import android.graphics.Canvas
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.Editable
import android.text.InputType
import android.view.View
import android.widget.EditText
import com.ljx.bysj.R


class KeyBoardUtils() {
    private var keyboardView: KeyboardView? = null
    var editText: EditText? = null
    var k1: Keyboard? = null //自定义的键盘

    constructor(keyboardView: KeyboardView,editText: EditText) : this() {
        this.keyboardView = keyboardView
        this.editText = editText
        this.editText!!.inputType = InputType.TYPE_NULL//取消弹出系统键盘
        k1 = Keyboard(this.editText!!.context,R.xml.keyboard)

        this.keyboardView!!.keyboard = k1 //设置显示键盘的样式
        this.keyboardView!!.isEnabled = true
        this.keyboardView!!.isPreviewEnabled = false
        this.keyboardView!!
        this.keyboardView!!.setOnKeyboardActionListener(listener) //设置键盘按钮被点击了的监听
    }

    interface OnEnsureListener {
        fun onEnsure()
    }


    private var onEnsureListener:OnEnsureListener? = null

    fun setOnEnsureListener(onEnsureListener: OnEnsureListener){
        this.onEnsureListener = onEnsureListener
    }


    private val listener: KeyboardView.OnKeyboardActionListener = object : KeyboardView.OnKeyboardActionListener {
        override fun onPress(primaryCode: Int) {}

        override fun onRelease(primaryCode: Int) {}

        override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
            val editable:Editable = editText!!.text
            val start:Int = editText!!.selectionStart
            when(primaryCode){
                Keyboard.KEYCODE_DELETE -> //点击了删除
                    if (editable.isNotEmpty()){
                        if (start > 0){
                            editable.delete(start-1,start)
                        }
                    }
                Keyboard.KEYCODE_CANCEL ->  //点击了清零
                    editable.clear()
                Keyboard.KEYCODE_DONE -> //点击了完成
                    onEnsureListener!!.onEnsure() //通过接口回调的方法，当点击确定时，可以调用这个方法
                else -> editable.insert(start, primaryCode.toChar().toString()) //直接插入
            }
        }

        override fun onText(text: CharSequence?) {}

        override fun swipeLeft() {}

        override fun swipeRight() {}

        override fun swipeDown() {}

        override fun swipeUp() {}

    }
    //显示键盘
    fun showKeyBoard(){
        var visibility:Int = keyboardView!!.visibility
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView!!.visibility = View.VISIBLE
        }
    }

    //隐藏键盘
    fun hideKeyBoard(){
        var visibility:Int = keyboardView!!.visibility
        if (visibility == View.INVISIBLE || visibility == View.VISIBLE) {
            keyboardView!!.visibility = View.GONE
        }
    }
}
