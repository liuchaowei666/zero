package com.ljx.bysj.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ljx.bysj.utils.FloatUtils

/**
 * 负责管理数据库的类
 * 主要对于表当中的内容进行操作，增删改查
 */
class DBManage {

    companion object {
        private var db: SQLiteDatabase? = null

        //初始化数据库对象
        fun initDB(context: Context) {
            var helper = DBOpenHelper(context)//得到帮助类对象
            db = helper.writableDatabase //得到数据库对象
        }


        /**
         * 读取数据库当中的数据，写入内存集合里
         * kind：表示收入或者支出
         */
        fun getTypeList(kind: Int): List<TypeBean> {
            val list: MutableList<TypeBean> = ArrayList()
            //读取typetb表中的数据
            val sql: String = "select * from typetb where kind = $kind"
            val cursor: Cursor = db!!.rawQuery(sql, null)
            //循环读取游标内容，存储到对象当中
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val typename = cursor.getString(cursor.getColumnIndex("typename"))
                val imgId = cursor.getInt(cursor.getColumnIndex("imgId"))
                val selectimgId = cursor.getInt(cursor.getColumnIndex("selectimgId"))
                val kind = cursor.getInt(cursor.getColumnIndex("kind"))
                val typeBean = TypeBean(id, typename, imgId, selectimgId, kind)
                list.add(typeBean)
            }
            return list
        }

        /**
         * 向记账表中插入一条元素
         */
        fun insertItemToAccounttb(bean: AccountBean) {
            var values: ContentValues = ContentValues()
            values.put("typename", bean.typename)
            values.put("selectimgId", bean.selectimgId)
            values.put("remark", bean.remark)
            values.put("count", bean.count)
            values.put("time", bean.time)
            values.put("year", bean.year)
            values.put("month", bean.month)
            values.put("day", bean.day)
            values.put("kind", bean.kind)
            db!!.insert("accounttb", null, values)

        }

        /**
         * 获取记录表当中的某一天的所有支出或者收入情况
         */
        fun getAListOneDayFromAtb(year: Int, month: Int, day: Int): List<AccountBean> {
            val list: MutableList<AccountBean> = ArrayList()
            var sql = "select * from accounttb where year = ? and month = ? and day = ? order by id desc"
            var cursor: Cursor =
                db!!.rawQuery(sql, arrayOf(year.toString() + "", month.toString() + "", day.toString() + ""))
            val test = arrayOf("$year", "$month", day.toString() + "")
            //遍历符合要求的每一行数据
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val typename = cursor.getString(cursor.getColumnIndex("typename"))
                val selectimgId = cursor.getInt(cursor.getColumnIndex("selectimgId"))
                var remark = cursor.getString(cursor.getColumnIndex("remark"))
                if (remark == null) {
                    remark = " "
                }
                var count = cursor.getFloat(cursor.getColumnIndex("count"))
                val time = cursor.getString(cursor.getColumnIndex("time"))
                val kind = cursor.getInt(cursor.getColumnIndex("kind"))
                if (kind == 0) {
                    count = -count
                }
                val accountBean = AccountBean(id, typename, selectimgId, remark, count, time, year, month, day, kind)
                list.add(accountBean)
            }
            return list
        }

        /**
         * 获取记录表当中的某一月的所有支出或者收入情况
         */
        fun getAListOneMonthFromAtb(year: Int, month: Int): List<AccountBean> {
            val list: MutableList<AccountBean> = ArrayList()
            var sql = "select * from accounttb where year = ? and month = ? order by id desc"
            var cursor: Cursor =
                db!!.rawQuery(sql, arrayOf(year.toString() + "", month.toString() + ""))
            val test = arrayOf("$year", "$month")
            //遍历符合要求的每一行数据
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val typename = cursor.getString(cursor.getColumnIndex("typename"))
                val selectimgId = cursor.getInt(cursor.getColumnIndex("selectimgId"))
                var remark = cursor.getString(cursor.getColumnIndex("remark"))
                if (remark == null) {
                    remark = " "
                }
                var count = cursor.getFloat(cursor.getColumnIndex("count"))
                val time = cursor.getString(cursor.getColumnIndex("time"))
                val kind = cursor.getInt(cursor.getColumnIndex("kind"))
                if (kind == 0) {
                    count = -count
                }
                val day = cursor.getInt(cursor.getColumnIndex("day"))
                val accountBean = AccountBean(id, typename, selectimgId, remark, count, time, year, month, day, kind)
                list.add(accountBean)
            }
            return list
        }

        /**
         * 获取某一天的支出或者收入的总金额
         */
        fun getSumMoneyOneDay(year: Int, month: Int, day: Int, kind: Int): Float {
            var total: Float = 0F
            var sql: String = "select sum(count) from accounttb where year = ? and month = ? and day = ? and kind = ?"
            val cursor = db!!.rawQuery(
                sql,
                arrayOf(year.toString() + "", month.toString() + "", day.toString() + "", kind.toString() + "")
            )
            //遍历
            if (cursor.moveToFirst()) {
                total = cursor.getFloat(cursor.getColumnIndex("sum(count)"))
            }
            return total
        }

        /**
         * 获取某一月的支出或者收入的总金额
         */
        fun getSumMoneyOneMonth(year: Int, month: Int, kind: Int): Float {
            var total: Float = 0F
            val sql: String = "select sum(count) from accounttb where year = ? and month = ? and kind = ?"
            val cursor = db!!.rawQuery(
                sql,
                arrayOf(year.toString() + "", month.toString() + "", kind.toString() + "")
            )
            //遍历
            if (cursor.moveToFirst()) {
                total = cursor.getFloat(cursor.getColumnIndex("sum(count)"))
            }
            if (kind == 0) {
                total = -total
            }
            return total
        }

        /**
         * 统计某月份收入或指出情况有多少笔  收入=1 支出=0
         */
        fun getCountItemOneMonth(year: Int, month: Int, kind: Int): Int {
            var total = 0
            val sql = "select count(count) from accounttb where year = ? and month = ? and kind = ?"
            val cursor =
                db!!.rawQuery(sql, arrayOf(year.toString() + "", month.toString() + "", kind.toString() + ""))
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(cursor.getColumnIndex("count(count)"))
                total = count
            }
            return total
        }

        /**
         * 获取某一年的支出或者收入的总金额
         */
        fun getSumMoneyOneYear(year: Int, kind: Int): Float {
            var total: Float = 0F
            var sql: String = "select sum(count) from accounttb where year = ? and kind = ?"
            val cursor = db!!.rawQuery(
                sql,
                arrayOf(year.toString() + "", kind.toString() + "")
            )
            //遍历
            if (cursor.moveToFirst()) {
                total = cursor.getFloat(cursor.getColumnIndex("sum(count)"))
            }
            return total
        }

        /**
         * 根据传入的id删除accounttb表当中的数据
         */
        fun deleteItemFromAccounttbById(id: Int): Int {
            val i = db!!.delete("accounttb", "id = ?", arrayOf(id.toString() + ""))
            return i
        }

        /**
         * 根据备注搜索收入或者支出的情况列表
         */
        fun getAccountListByRemarkFromAccounttb(key: String): List<AccountBean> {
            val list: MutableList<AccountBean> = ArrayList()
            val sql = "select * from accounttb where remark like '%$key%'"
            val cursor = db!!.rawQuery(sql, null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val typename = cursor.getString(cursor.getColumnIndex("typename"))
                val selectimgId = cursor.getInt(cursor.getColumnIndex("selectimgId"))
                val remark = cursor.getString(cursor.getColumnIndex("remark"))
                var count = cursor.getFloat(cursor.getColumnIndex("count"))
                val time = cursor.getString(cursor.getColumnIndex("time"))
                val kind = cursor.getInt(cursor.getColumnIndex("kind"))
                if (kind == 0) {
                    count = -count
                }
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                val month = cursor.getInt(cursor.getColumnIndex("month"))
                val day = cursor.getInt(cursor.getColumnIndex("day"))
                val accountBean = AccountBean(id, typename, selectimgId, remark, count, time, year, month, day, kind)
                list.add(accountBean)
            }
            return list
        }

        /**
         * 查询记账的表当中有几个年份记录
         */
        fun getYearListFromAccounttb(): MutableList<Int> {
            val list: MutableList<Int> = ArrayList()
            val sql = "select distinct(year) from accounttb order by year asc"
            val cursor = db!!.rawQuery(sql, null)
            while (cursor.moveToNext()) {
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                list.add(year)
            }
            return list
        }

        /**
         * 查询指定年份和月份的每一种类型收入或支出的总钱数
         */
        fun getChartListFromAccounttb(year: Int, month: Int, kind: Int): MutableList<ChartItemBean> {
            val list: MutableList<ChartItemBean> = ArrayList()
            val sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind)  //支出或收入总钱数
            val sql =
                "select typename,selectimgId,sum(count)as total from accounttb where year = ? and month = ? and kind = ? group by typename order by total desc"
            val cursor =
                db!!.rawQuery(sql, arrayOf(year.toString() + "", month.toString() + "", kind.toString() + ""))
            while (cursor.moveToNext()) {
                val selectimgId = cursor.getInt(cursor.getColumnIndex("selectimgId"))
                val typename = cursor.getString(cursor.getColumnIndex("typename"))
                var total = cursor.getFloat(cursor.getColumnIndex("total"))
                if (kind == 0) {
                    total = -total
                }
                //计算所占百分比 total/sumMoneyOneMonth
                val ratio = FloatUtils().div(total, sumMoneyOneMonth)
                val bean = ChartItemBean(selectimgId, typename, ratio, total)
                list.add(bean)
            }
            return list
        }

        /**
         *获取这个月当中某一天收入或支出最大的金额
         */
        fun getMaxMoneyOneDayInMonth(year: Int, month: Int, kind: Int): Float {
            val sql =
                "select sum(count) from accounttb where year = ? and month = ? and kind = ? group by day order by sum(count) desc"
            val cursor =
                db!!.rawQuery(sql, arrayOf(year.toString() + "", month.toString() + "", kind.toString() + ""))
            if (cursor.moveToFirst()) {
                return cursor.getFloat(cursor.getColumnIndex("sum(count)"))
            }
            return 0f
        }

        /**
         * 获取指定月份每一天收入或者支出的总钱数的集合
         */
        fun getSumCountOneDayInMonth(year: Int, month: Int, kind: Int): MutableList<BarChartItemBean> {
            val sql = "select day,sum(count) from accounttb where year = ? and month = ? and kind = ? group by day"
            val cursor =
                db!!.rawQuery(sql, arrayOf(year.toString() + "", month.toString() + "", kind.toString() + ""))
            val list: MutableList<BarChartItemBean> = ArrayList()
            while (cursor.moveToNext()){
                val day = cursor.getInt(cursor.getColumnIndex("day"))
                val dayCount = cursor.getFloat(cursor.getColumnIndex("sum(count)"))
                val itemBean = BarChartItemBean(year, month, day, dayCount)
                list.add(itemBean)
            }

            return list
        }

        /**
         *获取这个月当中某一天收入或支出最大的金额
         */
        fun getMaxOneDayInMonth(year: Int, month: Int, kind: Int): Int {
            val sql =
                "select day from accounttb where year = ? and month = ? and kind = ?  order by day desc"
            val cursor =
                db!!.rawQuery(sql, arrayOf(year.toString() + "", month.toString() + "", kind.toString() + ""))
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("day"))
            }
            return 0
        }

    }
}