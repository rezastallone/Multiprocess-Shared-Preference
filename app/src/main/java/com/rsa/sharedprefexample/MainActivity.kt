package com.rsa.sharedprefexample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rsa.sharedpreflibrary.SharedPrefProvider

class MainActivity : AppCompatActivity() {


//    setting values for preference
//    then retrieve the values from service in different process
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = SharedPrefProvider(PREF_NAME)
        sharedPref.setString(contentResolver,PREF_STRING ,"string value")
        sharedPref.setInt(contentResolver,PREF_INT,10)
        sharedPref.setBoolean(contentResolver,PREF_BOOL,true)
        sharedPref.setLong(contentResolver,PREF_LONG,100)
        sharedPref.setFloat(contentResolver,PREF_FLOAT,100.2f)
        sharedPref.setStringSet(contentResolver,PREF_SET, mutableSetOf("one","two","three"))

        val intent = Intent(this, TestService::class.java)
        startService(intent)
    }
}
