package com.rsa.sharedpreflibrary

import android.app.Application
import android.content.ContentResolver
import androidx.test.core.app.ApplicationProvider
import junit.framework.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SharedPrefProviderTest{

    val PREF_STRING = "prefstring"
    val PREF_INT = "prefint"
    val PREF_BOOL = "prefbool"
    val PREF_LONG = "prefloong"
    val PREF_FLOAT = "preffloat"
    val PREF_SET = "prefset"
    val NOT_EXIST_PREF = "NOT EXIST"

    val stringVal = "string value"
    val defStringVal = "default string value"
    val intVal = 10
    val defIntVal = -1
    val boolVal = true
    val defBoolVal = false
    val longVal = 100L
    val defLongVal = -2L
    val floatVal = 100.2f
    val defFloatVal = 100.2f
    val setVal = mutableSetOf("one","two","three")
    val defSetVal = mutableSetOf("")

    val contentResolver : ContentResolver

    init {
        Robolectric.setupContentProvider(SharedPrefProvider::class.java, SharedPrefProvider.AUTHORITY)
        contentResolver = ApplicationProvider.getApplicationContext<Application>().contentResolver
    }



    @Test
    fun testOperations(){
        val sharedPref = SharedPrefProvider()

        sharedPref.setString(contentResolver,PREF_STRING ,stringVal)
        val strRes = sharedPref.getString(contentResolver,PREF_STRING,defStringVal)
        assertEquals(stringVal,strRes)

        sharedPref.setInt(contentResolver,PREF_INT,intVal)
        val intRes = sharedPref.getInt(contentResolver,PREF_INT,defIntVal)
        assertEquals(intVal,intRes)

        sharedPref.setBoolean(contentResolver,PREF_BOOL,boolVal)
        val boolRes = sharedPref.getBoolean(contentResolver,PREF_BOOL,defBoolVal)
        assertEquals(boolVal,boolRes)

        sharedPref.setLong(contentResolver,PREF_LONG,longVal)
        val longRes = sharedPref.getLong(contentResolver,PREF_LONG,defLongVal)
        assertEquals(longVal,longRes)

        sharedPref.setFloat(contentResolver,PREF_FLOAT,floatVal)
        val floatRes = sharedPref.getFloat(contentResolver,PREF_FLOAT,defFloatVal)
        assertEquals(floatVal,floatRes)

        sharedPref.setStringSet(contentResolver,PREF_SET, setVal)
        val setRes = sharedPref.getStringSet(contentResolver,PREF_SET, defSetVal)
        assertEquals(setVal.size, setRes.size)

        assertEquals("{prefbool=true, prefloong=100, preffloat=100.2, prefstring=string value, prefint=10, prefset=[dHdv\n, b25l\n, dGhyZWU=\n]}",sharedPref.getAll(contentResolver).toString() )
    }

    @Test
    fun testDefaultVal(){
        val sharedPref = SharedPrefProvider()
        sharedPref.clear(contentResolver)

        val strRes = sharedPref.getString(contentResolver,PREF_STRING,defStringVal)
        assertEquals(defStringVal,strRes)

        val intRes = sharedPref.getInt(contentResolver,PREF_INT,defIntVal)
        assertEquals(defIntVal,intRes)

        val boolRes = sharedPref.getBoolean(contentResolver,PREF_BOOL,defBoolVal)
        assertEquals(defBoolVal,boolRes)

        val longRes = sharedPref.getLong(contentResolver,PREF_LONG,defLongVal)
        assertEquals(defLongVal,longRes)

        val floatRes = sharedPref.getFloat(contentResolver,PREF_FLOAT,defFloatVal)
        assertEquals(defFloatVal,floatRes)

        val setRes = sharedPref.getStringSet(contentResolver,PREF_SET, defSetVal)
        assertEquals(defSetVal.size, setRes.size)

    }

    @Test
    fun testClearPref() {
        val sharedPref = SharedPrefProvider()

        sharedPref.setString(contentResolver,PREF_STRING ,stringVal)

        sharedPref.clear(contentResolver)

        val strRes = sharedPref.getString(contentResolver,PREF_STRING,defStringVal)
        assertEquals(defStringVal,strRes)
    }

    @Test
    fun testContains() {
        val sharedPref = SharedPrefProvider()
        sharedPref.setString(contentResolver,PREF_STRING ,stringVal)

        assertTrue(sharedPref.contains(contentResolver,PREF_STRING))
        assertFalse(sharedPref.contains(contentResolver,NOT_EXIST_PREF))

    }
}
