package com.ljx.bysj.frag_record

import android.graphics.Color
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.Nullable
import com.ljx.bysj.R
import com.ljx.bysj.adapter.TypeBaseAdapter
import com.ljx.bysj.db.AccountBean
import com.ljx.bysj.db.TypeBean
import com.ljx.bysj.utils.KeyBoardUtils
import com.ljx.bysj.utils.RemarkDialog
import com.ljx.bysj.utils.SelectTimeDialog
import kotlinx.android.synthetic.main.fragment_expend.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * 记录页面当中的支出模块
 */
abstract class BaseRecordFragment() : Fragment(),View.OnClickListener {

    var keyboardView: KeyboardView? = null
    var countEt: EditText? =null
    var typeGv: GridView? = null
    var typeIv: ImageView? = null
    var typeTv: TextView? = null
    var remarkTv: TextView? = null
    var timeTv: TextView? = null
    var typeList: ArrayList<TypeBean> ? =null
    var adapter:TypeBaseAdapter? = null
    var accountBean: AccountBean? = null //将需要插入到记账本中的数据保存成对象的形式


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountBean = AccountBean()  //创建对象
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_expend, container, false)
        initView(view)
        setInitTime()
        //给GridView填充数据
        loadDataToGv()
        setGvListener() //设置GridView每一项的点击事件
        return view
    }

    //获取当前时间，显示在timeTv上
    private fun setInitTime() {
        val date = LocalDateTime.now()
        val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val time: String = sdf.format(date)
        timeTv!!.text = time
        accountBean!!.time = time

        val calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        accountBean!!.year = year
        accountBean!!.month = month
        accountBean!!.day = day
    }


    ////给GridView填充数据的方法
    open fun loadDataToGv() {
        typeList = ArrayList()
        adapter = TypeBaseAdapter(context!!, typeList!!)
        typeGv!!.adapter = adapter
    }
    /**
     * 设置GridView每一项的点击事件
     */
    private fun setGvListener() {
        typeGv!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            adapter!!.selectPos = position
            adapter!!.notifyDataSetInvalidated()//提示绘制发生变化
            val typeBean = typeList!![position]
            val typename = typeBean.typename
            typeTv!!.text = typename
            accountBean!!.typename = typename
            val selectimgId = typeBean.selectimgId
            typeIv!!.setImageResource(selectimgId)
            accountBean!!.selectimgId = selectimgId
        }
    }

    private fun initView(view: View?) {
        keyboardView = view!!.frag_record_keyboard
        countEt = view.frag_record_et_count
        typeIv = view.frag_record_iv
        typeGv = view.frag_record_gv
        typeTv = view.frag_record_tv_type
        remarkTv = view.frag_record_tv_remark
        timeTv = view.frag_record_tv_time

        remarkTv!!.setOnClickListener(this)
        timeTv!!.setOnClickListener(this)

        //让自定义软键盘显示出来
        val boardUtils = KeyBoardUtils(keyboardView!!,countEt!!)
        boardUtils.showKeyBoard()
        //设置接口，监听确定按钮被点击
        boardUtils.setOnEnsureListener(object : KeyBoardUtils.OnEnsureListener {
            override fun onEnsure() { //点击了确定按钮
                //获取输入钱数
                val countStr = countEt!!.text.toString()
                if (TextUtils.isEmpty(countStr) || countStr.equals(0)){
                    activity!!.finish()
                    return
                }
                val count = countStr.toFloat()
                accountBean!!.count = count
                //获取记录的信息，保存在数据库中
                saveAccountToDB()
                //返回上一级页面
                activity!!.finish()
            }

        })
    }

    //让子类一定要重写这个方法
    abstract fun saveAccountToDB()

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.frag_record_tv_time -> {
                showTimeDialog()
            }
            R.id.frag_record_tv_remark ->{
                showRemarkDialog()
            }
        }
    }

    //弹出选择时间对话框
    private fun showTimeDialog() {
        val dialog = SelectTimeDialog(context!!)
        dialog.show()
        //设定确定按钮被点击了的监听器
        dialog.setOnEnsureListener(object : SelectTimeDialog.OnEnsureListener{
            override fun onEnsure(time: String, year: Int, month: Int, day: Int) {
                timeTv!!.text = time
                accountBean!!.time = time
                accountBean!!.year = year
                accountBean!!.month = month
                accountBean!!.day = day
            }

        })
    }

    //弹出备注对话框
    private fun showRemarkDialog() {
        val dialog = RemarkDialog(context!!)
        dialog.show()
        dialog.setDialogSize()

        dialog.setOnEnsureListener(object : KeyBoardUtils.OnEnsureListener {
            override fun onEnsure() {
                val msg = dialog.getEditText()
                if (!TextUtils.isEmpty(msg)){
                    remarkTv!!.text = msg
                    accountBean!!.remark = msg
                }
                if (msg != "备注"){
                    remarkTv!!.setTextColor(Color.parseColor("#2196f7"))
                }
                if (TextUtils.isEmpty(msg)){
                    remarkTv!!.text = "备注"
                    accountBean!!.remark = msg
                }
                dialog.cancel()
            }

        })
    }

}