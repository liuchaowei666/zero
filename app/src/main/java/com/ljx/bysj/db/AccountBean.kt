package com.ljx.bysj.db

/**
 * 描述记录一条数据的相关内容类
 */
class AccountBean {
    var id: Int = 0
    var typename: String? = null // 类型名称
    var selectimgId: Int = 0 //被选中图片ID
    var remark: String? = null //备注
    var count: Float = 0F //钱数
    var time: String? = null //保存时间字符串
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var kind: Int = 0 //收入1，支出0

    constructor() {}
    constructor(
        id: Int, typename: String?, selectimgId: Int, remark: String, count: Float,
        time: String, year: Int, month: Int, day: Int, kind: Int
    ) {
        this.id = id
        this.typename = typename
        this.selectimgId = selectimgId
        this.remark = remark
        this.count = count
        this.time = time
        this.year = year
        this.month = month
        this.day = day
        this.kind = kind
    }

}