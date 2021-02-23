package com.ljx.bysj

import android.app.Application
import com.ljx.bysj.db.DBManage

/**
 * 表示全局应用的类
 */
class UniteApp: Application() {
    override fun onCreate() {
        super.onCreate()
        //初始化数据库
        DBManage.initDB(applicationContext)
    }
}