package com.ljx.bysj

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_theme.*
import kotlinx.android.synthetic.main.navigation_view_headerlayout.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.IntentCompat


class ThemeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var blueBtn:Button
    lateinit var pinkBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)
        initview()
    }


    private fun initview() {
        blueBtn = theme_blue
        pinkBtn = theme_pink

        blueBtn.setOnClickListener(this)
        pinkBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.theme_blue -> {

            }
            R.id.theme_pink -> {
            }
        }
    }
}