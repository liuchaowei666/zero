package com.ljx.bysj

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import cn.bmob.v3.Bmob
import com.google.android.material.navigation.NavigationView
import com.ljx.bysj.adapter.AccountAdapter
import com.ljx.bysj.db.AccountBean
import com.ljx.bysj.db.DBManage
import com.ljx.bysj.utils.DetailDialog
import com.ljx.bysj.utils.MoreDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_layout.*
import kotlinx.android.synthetic.main.item_mainlv_top.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var todayLv: ListView? = null //展示今日收支情况的ListView
    var searchIv: ImageView? = null
    var editBtn: ImageButton? = null
    var moreBtn: ImageButton? = null

    //声明数据类型
    var mDatas: MutableList<AccountBean>? = null
    var adapter: AccountAdapter? = null
    var year = 0
    var month = 0
    var day = 0

    //头布局相关控件
    private var analysisTv: TextView? = null
    var topExpendTv: TextView? = null
    var topIncomeTv: TextView? = null
    var topSurplusTv: TextView? = null
    var topConTv: TextView? = null
    private var topShowIv: ImageView? = null
    lateinit var topRl: RelativeLayout
    private var moreIv: ImageView? = null

    //侧滑菜单
    lateinit var dl: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var headView: View
    var profileIv: ImageView? = null
    lateinit var nickNameTv: TextView

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //沉浸状态栏
        val window = window
        val decorView = window.decorView
        val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        decorView.systemUiVisibility = option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        //初始化bmob
        Bmob.initialize(this, "118b089613cb9b1bc322a9dbbc2be3a4")

        initTime()
        initView()
        //添加listView的头布局
        addLvHeaderView()
        ////侧滑菜单
        initDrawerView()
        mDatas = ArrayList<AccountBean>()
        //设置适配器，加载每一行数据到列表中
        adapter = AccountAdapter(this, mDatas!!)
        todayLv!!.adapter = adapter
        analysisTv!!.setOnClickListener(this)
        topSurplusTv!!.setOnClickListener(this)
        topShowIv!!.setOnClickListener(this)
        moreIv!!.setOnClickListener(this)
    }

    private fun initDrawerView() {
        dl = drawerLayout
        dl.fitsSystemWindows = true
        dl.clipToPadding = false
        navigationView = findViewById(R.id.navigationView)
        headView = navigationView.getHeaderView(0)
        profileIv = headView.findViewById(R.id.header_img)
        nickNameTv = headView.findViewById(R.id.header_tv)


        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.themeFragment -> {
                    val intent = Intent(this, ThemeActivity().javaClass)
                    startActivity(intent)
                }
                R.id.aboutFragment -> {
                    val intent = Intent(this, AboutActivity().javaClass)
                    startActivity(intent)
                }
            }
            true
        }

        profileIv!!.setOnClickListener(this)

    }

    /**
     * 初始化自带的View的方法
     */
    private fun initView() {
        todayLv = main_lv
        editBtn = main_btn_add
        moreBtn = main_btn_more

        editBtn!!.setOnClickListener(this)
        moreBtn!!.setOnClickListener(this)
        setLongClickListener()
    }

    /**
     * 设置ListView的长按事件
     */
    private fun setLongClickListener() {
        todayLv!!.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                if (position == 0) {//点击了头布局
                    return false
                } else {
                    val pos = position - 1
                    val selectBean = mDatas!![pos] //获取正在被点击的这条记录
                    //弹出dialog
                    showDetailDialog(selectBean, mDatas, adapter)
                }
                return false
            }
        }

    }


    private fun showDetailDialog(selectBean: AccountBean, mDatas: MutableList<AccountBean>?, adapter: AccountAdapter?) {
        val dialog = DetailDialog(this)
        dialog.show()
        dialog.setDialogSize()
        dialog.getSelectBean(selectBean, mDatas!!, adapter)
        dialog.setOnDeleteListener(object : DetailDialog.OnDeleteListener {
            override fun onDelete(info: String, Income: Float, Expend: Float) {
                topConTv!!.text = info
                topIncomeTv!!.text = Income.toString()
                topExpendTv!!.text = Expend.toString()
                topSurplusTv!!.text = (Income - Expend).toString()
            }

        })
    }


    /**
     * 给ListView添加头布局的方法
     */
    private fun addLvHeaderView() {
        //给布局转换成View对象
        val headerView = layoutInflater.inflate(R.layout.item_mainlv_top, null)
        todayLv!!.addHeaderView(headerView)
        topRl = item_mainlv_top_rl
        topRl.background.alpha = 100
        //查找头布局可用控件
        searchIv = main_iv_search
        analysisTv = item_mainlv_top_tv4
        topExpendTv = item_mainlv_top_expend
        topIncomeTv = item_mainlv_top_income
        topSurplusTv = item_mainlv_top_surplus
        topConTv = item_mainlv_top_tv_day
        topShowIv = item_mainlv_top_iv_hide
        moreIv = item_mainlv_iv_more
        searchIv!!.setOnClickListener(this)
    }

    /**
     * 获取今日的具体时间
     */
    private fun initTime() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH] + 1
        day = calendar[Calendar.DAY_OF_MONTH]
    }

    //表示当activity获取焦点时，会调用的方法
    override fun onResume() {
        super.onResume()
        loadDBdata()
        setTopTvShow()
    }


    /**
     * 设置头布局当中文本内容的显示
     */
    private fun setTopTvShow() {
        //获取今日支出和收入总金额，显示在View中
        var IncomeOneDay = DBManage.getSumMoneyOneDay(year, month, day, 1)
        var ExpendOneDay = DBManage.getSumMoneyOneDay(year, month, day, 0)
        var infoOneDay = "今日支出 $ExpendOneDay 收入 $IncomeOneDay"
        topConTv!!.text = infoOneDay
        //获取本月支出和收入总金额，显示在View中
        var IncomeOneMonth = DBManage.getSumMoneyOneMonth(year, month, 1)
        var ExpendOneMonth = DBManage.getSumMoneyOneMonth(year, month, 0)
        topExpendTv!!.text = ExpendOneMonth.toString()
        topIncomeTv!!.text = IncomeOneMonth.toString()
        topSurplusTv!!.text = (IncomeOneMonth - ExpendOneMonth).toString()
    }

    private fun loadDBdata() {
        val list = DBManage.getAListOneDayFromAtb(year, month, day)
        mDatas!!.clear()
        mDatas!!.addAll(list)
        adapter!!.notifyDataSetChanged()
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.main_iv_search -> {
                val intent = Intent(this, SearchActivity().javaClass)
                startActivity(intent)
            }
            R.id.item_mainlv_top_tv4 -> {
                val intent = Intent(this, ChartActivity().javaClass)
                startActivity(intent)
            }
            R.id.main_btn_add -> {
                val intent = Intent(this, RecordActivity().javaClass)
                startActivity(intent)
            }
            R.id.main_btn_more -> {
                val moreDialog = MoreDialog(this)
                moreDialog.show()
                moreDialog.setDialogSize()
            }
            R.id.item_mainlv_top_iv_hide -> {
                //切换TextView明文密文
                toggleShow()
            }
            R.id.item_mainlv_iv_more -> {
                if (!dl.isDrawerOpen(navigationView)) {
                    dl.openDrawer(navigationView)
                }
            }
            R.id.header_img -> {
                val intent = Intent(this, LoginActivity().javaClass)
                startActivity(intent)
            }

        }
    }

    var isShow: Boolean = true
    private fun toggleShow() {
        //切换TextView明文密文
        if (isShow) {
            val passwordTransformationMethod = PasswordTransformationMethod.getInstance()
            topIncomeTv!!.transformationMethod = passwordTransformationMethod
            topExpendTv!!.transformationMethod = passwordTransformationMethod
            topSurplusTv!!.transformationMethod = passwordTransformationMethod
            topShowIv!!.setImageResource(R.mipmap.ih_hide)
            isShow = false
        } else {
            val hideReturnsTransformationMethod = HideReturnsTransformationMethod.getInstance()
            topIncomeTv!!.transformationMethod = hideReturnsTransformationMethod
            topExpendTv!!.transformationMethod = hideReturnsTransformationMethod
            topSurplusTv!!.transformationMethod = hideReturnsTransformationMethod
            topShowIv!!.setImageResource(R.mipmap.ih_show)
            isShow = true
        }
    }

    fun afterDelete(selectBean: AccountBean, selectmDatas: MutableList<AccountBean>, selectAdapter: AccountAdapter) {
        selectmDatas.remove(selectBean)
        mDatas = selectmDatas
        selectAdapter.notifyDataSetChanged()
        adapter = selectAdapter
    }


}