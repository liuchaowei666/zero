package com.ljx.bysj.db

class BarChartItemBean {
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var sumcount: Float = 0f

    constructor(year: Int, month: Int, day: Int, sumcount: Float) {
        this.year = year
        this.month = month
        this.day = day
        this.sumcount = sumcount
    }
}