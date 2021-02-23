package com.ljx.bysj.db

class ChartItemBean {
    var sImageId = 0
    lateinit var type:String
    var ratio = 0f
    var totalCount = 0f

    constructor(){}
    constructor(sImageId: Int,type: String,ratio: Float,totalCount: Float){
        this.sImageId = sImageId
        this.type = type
        this.ratio = ratio
        this.totalCount = totalCount
    }
}