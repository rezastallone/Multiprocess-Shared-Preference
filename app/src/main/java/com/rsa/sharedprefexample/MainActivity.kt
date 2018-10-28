package com.rsa.sharedprefexample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rsa.sharedpreflibrary.SharedPrefProvider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val PREF_STRING = "prefstring"
        val PREF_INT = "prefint"
        val PREF_BOOL = "prefbool"
        val PREF_LONG = "prefloong"
        val PREF_FLOAT = "preffloat"
        val PREF_SET = "prefset"
        setContentView(R.layout.activity_main)
        val sharedPref = SharedPrefProvider("haha")
        sharedPref.setString(contentResolver,PREF_STRING ,"string value")
        sharedPref.setInt(contentResolver,PREF_INT,10)
        sharedPref.setBoolean(contentResolver,PREF_BOOL,true)
        sharedPref.setLong(contentResolver,PREF_LONG,100)
        sharedPref.setFloat(contentResolver,PREF_FLOAT,100.2f)
        sharedPref.setStringSet(contentResolver,PREF_SET, mutableSetOf("satu","dua","tiga"))

        val intent = Intent(this, TestService::class.java)
        startService(intent)
    }
}
