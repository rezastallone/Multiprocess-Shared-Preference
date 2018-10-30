package com.rsa.sharedprefexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.rsa.sharedpreflibrary.SharedPrefProvider

class TestService : Service() {

    val sharedPref = SharedPrefProvider(PREF_NAME)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        printPref()
//        test removing a preference
        sharedPref.remove(contentResolver,PREF_STRING)
        printPref()
//        test clearing whole preference
        sharedPref.clear(contentResolver)
//        testing default value
        printPrefDefault()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun printPref(){
        val stringVal = sharedPref.getString(contentResolver,PREF_STRING )
        val intVal = sharedPref.getInt(contentResolver,PREF_INT)
        val boolVal = sharedPref.getBoolean(contentResolver,PREF_BOOL)
        val longVal = sharedPref.getLong(contentResolver,PREF_LONG)
        val floatVal = sharedPref.getFloat(contentResolver,PREF_FLOAT)
        val setVal = sharedPref.getStringSet(contentResolver,PREF_SET)

        Log.d("PREF", stringVal)
        Log.d("PREF", intVal.toString())
        Log.d("PREF", boolVal.toString())
        Log.d("PREF", longVal.toString())
        Log.d("PREF", floatVal.toString())
        Log.d("PREF", setVal.toString())
        Log.d("PREF", "Contains $PREF_STRING ? ${sharedPref.contains(contentResolver,PREF_STRING)}")
        Log.d("PREF", "Contains DUMMY ? ${sharedPref.contains( contentResolver,"DUMMY")}")
        Log.d("PREF", "PREF ALL RESULT ${sharedPref.getAll(contentResolver)}")
    }

    private fun printPrefDefault(){
        val stringVal = sharedPref.getString(contentResolver, PREF_STRING ,"default string value")
        val intVal = sharedPref.getInt(contentResolver, PREF_INT,5)
        val boolVal = sharedPref.getBoolean(contentResolver, PREF_BOOL,false)
        val longVal = sharedPref.getLong(contentResolver, PREF_LONG,50)
        val floatVal = sharedPref.getFloat(contentResolver, PREF_FLOAT,50.2f)
        val setVal = sharedPref.getStringSet(contentResolver, PREF_SET, mutableSetOf("tiga","dua","satu"))

        Log.d("PREF", stringVal)
        Log.d("PREF", intVal.toString())
        Log.d("PREF", boolVal.toString())
        Log.d("PREF", longVal.toString())
        Log.d("PREF", floatVal.toString())
        Log.d("PREF", setVal.toString())
        Log.d("PREF", "Contains $PREF_STRING ? ${sharedPref.contains(contentResolver,PREF_STRING)}")
        Log.d("PREF", "Contains DUMMY ? ${sharedPref.contains( contentResolver,"DUMMY")}")
        Log.d("PREF", "PREF ALL RESULT ${sharedPref.getAll(contentResolver)}")
    }

}