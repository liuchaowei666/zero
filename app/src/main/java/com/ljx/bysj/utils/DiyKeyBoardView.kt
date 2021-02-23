package com.ljx.bysj.utils

import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import android.R
import android.graphics.*
import android.graphics.Paint.FontMetricsInt

import android.graphics.drawable.Drawable
import android.graphics.Typeface
import java.security.Key


class DiyKeyBoardView(context: Context?, attrs: AttributeSet?) : KeyboardView(context, attrs) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val keyboard = keyboard ?: return
        val keys = keyboard.keys
        if (keys != null && keys.size > 0){
            val paint = Paint()
            paint.textAlign = Paint.Align.CENTER
            val font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.typeface = font
            paint.isAntiAlias = true
            for (key:Keyboard.Key in keys){
                if (key.codes[0] == -4){
                }
            }
        }
    }
}