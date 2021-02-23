package com.ljx.bysj.db

/**
 * 表示收入或支出具体类型的类
 */
class TypeBean {
    var id: Int = 0
    var typename: String? = null // 类型名称
    var imgId: Int = 0 //未被选中图片ID
    var selectimgId: Int = 0 //被选中图片ID
    var kind: Int = 0 //收入1，支出0

    constructor(){}

    constructor(id: Int, typename: String, imgId: Int, selectimgId: Int, kind: Int) {
        this.id = id
        this.typename = typename
        this.imgId = imgId
        this.selectimgId = selectimgId
        this.kind = kind
    }

}