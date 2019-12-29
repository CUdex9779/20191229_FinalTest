package com.example.a20191229_finaltest

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    val mContext = this

    abstract fun setEvents()
    abstract fun setValues()

}