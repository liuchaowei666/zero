package com.ljx.bysj.db

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ljx.bysj.R
import java.sql.Array
import java.util.*
import kotlin.collections.ArrayList

class DBOpenHelper(
    context: Context?,
) : SQLiteOpenHelper(context, "tally.db", null, 1) {

    //创建数据库的方法，只有项目第一次运行时会被调用
    override fun onCreate(db: SQLiteDatabase?) {
        //创建表示类型的表
        var sql: String = "create table typetb(id integer primary key autoincrement,typename varchar(10)," +
                "imgId integer,selectimgId integer,kind integer)"
        db!!.execSQL(sql)
        insertType(db)
        //记账表
        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10)," +
                "selectimgId integer,remark varchar(50),count float,time varchar(50),year integer," +
                "month integer,day integer,kind integer)"
        db.execSQL(sql)
    }

    private fun insertType(db: SQLiteDatabase?) {
        //向type表中插入元素
        var sql: String = "insert into typetb (typename,imgId,selectimgId,kind) values (?,?,?,?)"
        //支出
        db!!.execSQL(sql, arrayOf<Any>("三餐", R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,0))
        db.execSQL(sql, arrayOf<Any>("交通", R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,0))
        db.execSQL(sql, arrayOf<Any>("服饰", R.mipmap.ic_fushi,R.mipmap.ic_fushi_fs,0))
        db.execSQL(sql, arrayOf<Any>("话费", R.mipmap.ic_huafei,R.mipmap.ic_huafei_fs,0))
        db.execSQL(sql, arrayOf<Any>("住房", R.mipmap.ic_zhufang,R.mipmap.ic_zhufang_fs,0))
        db.execSQL(sql, arrayOf<Any>("日用品", R.mipmap.ic_riyongpin,R.mipmap.ic_riyongpin_fs,0))
        db.execSQL(sql, arrayOf<Any>("医疗", R.mipmap.ic_yiliao,R.mipmap.ic_yiliao_fs,0))
        db.execSQL(sql, arrayOf<Any>("学习", R.mipmap.ic_xuexi,R.mipmap.ic_xuexi_fs,0))
        db.execSQL(sql, arrayOf<Any>("零食", R.mipmap.ic_lingshi,R.mipmap.ic_lingshi_fs,0))
        db.execSQL(sql, arrayOf<Any>("美妆", R.mipmap.ic_meizhuang,R.mipmap.ic_meizhuang_fs,0))
        db.execSQL(sql, arrayOf<Any>("加油", R.mipmap.ic_jiayou,R.mipmap.ic_jiayou_fs,0))
        db.execSQL(sql, arrayOf<Any>("数码", R.mipmap.ic_shuma,R.mipmap.ic_shuma_fs,0))
        db.execSQL(sql, arrayOf<Any>("运动", R.mipmap.ic_yundong,R.mipmap.ic_yundong_fs,0))
        db.execSQL(sql, arrayOf<Any>("烟酒", R.mipmap.ic_yanjiu,R.mipmap.ic_yanjiu_fs,0))
        db.execSQL(sql, arrayOf<Any>("娱乐", R.mipmap.ic_yule,R.mipmap.ic_yule_fs,0))
        db.execSQL(sql, arrayOf<Any>("其它", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,0))
        //收入
        db!!.execSQL(sql, arrayOf<Any>("工资", R.mipmap.in_gongzi,R.mipmap.in_gongzi_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("生活费", R.mipmap.in_shenghuofei,R.mipmap.in_shenghuofei_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("抢红包", R.mipmap.in_hongbao,R.mipmap.in_hongbao_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("分红", R.mipmap.in_fenhong,R.mipmap.in_fenhong_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("兼职", R.mipmap.in_jianzhi,R.mipmap.in_jianzhi_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("股票", R.mipmap.in_gupiao,R.mipmap.in_gupiao_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("基金", R.mipmap.in_jijin,R.mipmap.in_jijin_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("压岁钱", R.mipmap.in_yasuiqian,R.mipmap.in_yasuiqian_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("奖学金", R.mipmap.in_jiangxuejin,R.mipmap.in_jiangxuejin_fs,1))
        db!!.execSQL(sql, arrayOf<Any>("其它", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,1))

    }

    //数据库版本在更新时发生改变会调用此方法
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}