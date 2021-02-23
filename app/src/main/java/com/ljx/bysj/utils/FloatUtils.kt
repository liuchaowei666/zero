package com.ljx.bysj.utils

import android.icu.math.BigDecimal

class FloatUtils {
    fun div(f1: Float, f2: Float): Float {
        val f3 = f1 / f2
        val bd = BigDecimal(f3.toDouble())
        val fl = bd.setScale(4, 4).toFloat()
        return fl
    }

    fun ratioToPercent(fl: Float): String {
        val f = fl * 100
        val bigDecimal = BigDecimal(f.toDouble())
        val s = bigDecimal.setScale(2, 4).toString()
        return "$s%"
    }
}