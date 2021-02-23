package com.ljx.bysj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.ljx.bysj.bmob.myUser
import kotlinx.android.synthetic.main.activity_regist.*
import java.util.*

class RegistActivity : AppCompatActivity() ,View.OnClickListener {

    lateinit var usernameEt: EditText
    lateinit var passwordEt: EditText
    lateinit var registBtn: Button
    lateinit var loginBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        usernameEt = regist_username
        passwordEt = regist_password
        registBtn = regist_registBtn
        loginBtn = regist_loginBtn



        registBtn.setOnClickListener(this)
        loginBtn.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.regist_registBtn -> {
                val username = usernameEt.text.toString()
                val password = passwordEt.text.toString()
                //非空验证
                if (username.isEmpty()){
                    Toast.makeText(this,"用户名不能为空！",Toast.LENGTH_SHORT).show()
                    usernameEt.requestFocus()
                    return
                }
                if (password.isEmpty()){
                    Toast.makeText(this,"密码不能为空！",Toast.LENGTH_SHORT).show()
                    passwordEt.requestFocus()
                    return
                }
                //向表中添加一条数据
                val bmobUser = myUser()
                bmobUser.username = username
                bmobUser.setPassword(password)
                val defaultNickname = UUID.randomUUID().toString().replace("-", "")
                bmobUser.nickname = "用户$defaultNickname"
                bmobUser.signUp(object : SaveListener<myUser>(){
                    override fun done(p0: myUser?, p1: BmobException?) {
                        if (p1 == null) {
                            val toast = Toast.makeText(applicationContext, "注册成功", Toast.LENGTH_SHORT)
                            toast.show()
                            val intent = Intent(LoginActivity(), MainActivity().javaClass)
                            startActivity(intent)
                            MainActivity().nickNameTv.text = defaultNickname
                            finish()
                        }else {
                            Log.i("bmob", "$p1")
                        }
                    }

                })
            }
            R.id.regist_loginBtn -> {
                val intent = Intent(this, LoginActivity().javaClass)
                startActivity(intent)
            }
        }
    }


}