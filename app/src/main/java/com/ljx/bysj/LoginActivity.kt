package com.ljx.bysj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.ljx.bysj.bmob.myUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.navigation_view_headerlayout.*
import okhttp3.internal.publicsuffix.PublicSuffixDatabase

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var usernameEt: EditText
    lateinit var passwordEt: EditText
    lateinit var registBtn: Button
    lateinit var loginBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEt = username
        passwordEt = password
        registBtn = login_registbtn
        loginBtn = login_loginbtn


        registBtn.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val mainNavHeaderView = layoutInflater.inflate(R.layout.navigation_view_headerlayout, null)
        val navHeaderTv: TextView = mainNavHeaderView.findViewById(R.id.header_tv)
        when (v!!.id) {
            R.id.login_loginbtn -> {
                val username = usernameEt.text.toString()
                val password = passwordEt.text.toString()
                //非空验证
                if (username.isEmpty()) {
                    Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show()
                    usernameEt.requestFocus()
                    return
                }
                if (password.isEmpty()) {
                    Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show()
                    passwordEt.requestFocus()
                    return
                }
                //向表中添加一条数据
                val bmobUser = myUser()
                bmobUser.username = username
                bmobUser.setPassword(password)
                bmobUser.login(object : SaveListener<myUser>() {

                    override fun done(p0: myUser?, p1: BmobException?) {
                        if (p1 == null) {
                            val getUser: myUser = BmobUser.getCurrentUser(myUser()::class.java)

                            val toast = Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT)
                            toast.show()
                            Log.i("bmob", "done: ${getUser.nickname}")
                        } else {
                            Log.i("bmob", "$p1")
                        }
                    }
                })

//                val bmobQuery = BmobQuery<myUser>()
//                bmobQuery.addWhereEqualTo("username", username)
//                bmobQuery.findObjects(object : FindListener<myUser>() {
//                    lateinit var loginNickname: String
//                    override fun done(p0: MutableList<myUser>?, p1: BmobException?) {
//                        if (p1 == null) {
//                            for (i in p0!!.indices) {
//                                loginNickname = p0[i].nickname.toString()
//                                Log.i("nickname", "done: $loginNickname")
//                            }
//                            finish()
//                            val intent = Intent(this@LoginActivity, MainActivity().javaClass)
//                            startActivity(intent)
//                            navHeaderTv.text = loginNickname
//
//                        }
//                    }
//                })
            }
        }
    }
}