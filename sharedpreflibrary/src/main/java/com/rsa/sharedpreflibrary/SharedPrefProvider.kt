package com.rsa.sharedpreflibrary

import android.content.*
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

class SharedPrefProvider(preferenceName:String = "DEFAULT", mode : Int = MODE_PRIVATE) : ContentProvider() {

    companion object {
        val AUTHORITY = "com.rsa.sharedpreflibrary"
    }

    private val PREF_NAME = preferenceName
    private val PREF_READ_MODE = mode
    private val CONTENT_URI_PATH = Uri.parse("content://$AUTHORITY/sharedpref")
    private val STRING_URI_PATH = "1"
    private val INT_URI_PATH = "2"
    private val BOOLEAN_URI_PATH = "3"
    private val FLOAT_URI_PATH = "4"
    private val LONG_URI_PATH = "5"
    private val STRINGSET_URI_PATH = "6"
    private val CONTAINS_URI_PATH = "7"
    private val ALLPREF_URI_PATH = "8"
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreate(): Boolean {
        sharedPreference = context.getSharedPreferences(PREF_NAME, PREF_READ_MODE)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if ( values != null ){
            val pref = uri.pathSegments[2]
            val category = uri.pathSegments[1]
            val editor = sharedPreference.edit()
            when ( category ){
                STRING_URI_PATH -> editor.putString(pref, values.getAsString(pref))
                INT_URI_PATH -> editor.putInt(pref, values.getAsInteger(pref))
                BOOLEAN_URI_PATH -> editor.putBoolean(pref, values.getAsBoolean(pref))
                FLOAT_URI_PATH -> editor.putFloat(pref, values.getAsFloat(pref))
                LONG_URI_PATH -> editor.putLong(pref, values.getAsLong(pref))
                STRINGSET_URI_PATH -> editor.putStringSet(pref, mutableSetOf(values.getAsString(pref)))
            }
            editor.apply()
        }
        val _uri = ContentUris.withAppendedId(CONTENT_URI_PATH, 1)
        return _uri
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val pref = uri.pathSegments[2]
        var cursor = MatrixCursor(arrayOf(pref))
        val category = uri.pathSegments[1]
        val defaultVal = uri.pathSegments[3]
        when (category) {
            STRING_URI_PATH -> cursor.addRow(arrayOf(sharedPreference.getString(pref,defaultVal.substring(1))))
            INT_URI_PATH -> cursor.addRow(arrayOf(sharedPreference.getInt(pref, defaultVal.toInt())))
            BOOLEAN_URI_PATH -> cursor.addRow(arrayOf(sharedPreference.getBoolean(pref, defaultVal.toInt() > 1)))
            FLOAT_URI_PATH -> cursor.addRow(arrayOf(sharedPreference.getFloat(pref, defaultVal.toFloat())))
            LONG_URI_PATH -> cursor.addRow(arrayOf(sharedPreference.getLong(pref, defaultVal.toLong())))
            STRINGSET_URI_PATH -> {
                val set = sharedPreference.getStringSet(pref,null)
                if ( set != null ){
                    cursor.addRow(arrayOf(set.toString()))
                }
            }
            CONTAINS_URI_PATH  -> cursor.addRow(arrayOf(sharedPreference.contains(pref)))
            ALLPREF_URI_PATH -> {
                val all = sharedPreference.all
                cursor = MatrixCursor(all.keys.toTypedArray())
                cursor.addRow(all.values.toTypedArray())
            }
        }
        return cursor
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        insert(uri, values)
        return 1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val count : Int
        if ( uri.pathSegments.size > 2 ){
            count = if ( sharedPreference.edit().remove(uri.pathSegments[2]).commit() ) 1 else 0
        } else {
            count = sharedPreference.all.size
            sharedPreference.edit().clear().apply()
        }
        return count
    }

    override fun getType(uri: Uri) = uri.toString()

    private fun getUri(uri: String, pref: String, defaultVal:String=""): Uri {
        return Uri.parse("$CONTENT_URI_PATH/$uri/$pref/$defaultVal")
    }

    private fun getClearUri() = getUri("0","")

    private fun getRemoveUri(pref: String) = getUri("0", pref, "=")

    private fun getStringUri(pref: String, defaultVal:String = "") = getUri("1", pref, "=$defaultVal")

    private fun getIntUri(pref: String, defaultVal : Int = 0) = getUri("2", pref, defaultVal.toString())

    private fun getBooleanUri(pref: String, defaultVal : Boolean = false) = getUri("3", pref, (if ( defaultVal ) 1 else 0 ).toString())

    private fun getFloatUri(pref: String, defaultVal : Float = 0f) = getUri("4", pref, defaultVal.toString())

    private fun getLongUri(pref: String, defaultVal : Long = 0) = getUri("5", pref, defaultVal.toString())

    fun getStringSetUri(pref: String) = getUri("6", pref, "=")

    private fun getContainsUri(pref: String) = getUri("7", pref, "=")

    private fun getAllUri() = getUri("8", "=", "=")

    fun getAll(contentResolver: ContentResolver): MutableMap<String, *>{
        val result = mutableMapOf<String, Any>()
        val c = contentResolver.query(getAllUri(),null,null,null,null)
        if ( c != null && c.moveToFirst() ){
            for ( i in 0 until c.columnCount ){
                val colName = c.getColumnName(i)
                val colVal = c.getString(i)
                result.put(colName, colVal)
            }
        }
        c.close()
        return result
    }

    fun contains(contentResolver: ContentResolver,pref: String) : Boolean{
        val c = contentResolver.query(getContainsUri(pref),null,null,null,null)
        if ( c != null && c.moveToFirst() ){
            return c.getString(c.getColumnIndex(pref)).toBoolean()
        }
        c.close()
        return false
    }

    fun getString(contentResolver: ContentResolver,pref:String, defaultVal : String = ""):String{
        val c = contentResolver.query(getStringUri(pref, defaultVal),null,null,null,null)
        if ( c != null && c.moveToFirst() ){
            return c.getString(c.getColumnIndex(pref))
        }
        c.close()
        return defaultVal
    }

    fun setString(contentResolver: ContentResolver,pref: String, value : String ): Uri? {
        val contentValue = ContentValues()
        contentValue.put(pref, value)
        return contentResolver.insert(getStringUri(pref), contentValue)
    }

    fun getInt(contentResolver: ContentResolver,pref:String, defaultVal : Int = 0) : Int{
        val c = contentResolver.query(getIntUri(pref, defaultVal),null,null,null,null)
        if ( c != null && c.moveToFirst() ){
            return c.getInt(c.getColumnIndex(pref))
        }
        c.close()
        return defaultVal
    }

    fun setInt(contentResolver: ContentResolver,pref: String, value : Int ){
        val contentValue = ContentValues()
        contentValue.put(pref, value)
        contentResolver.insert(getIntUri(pref), contentValue)
    }

    fun getBoolean(contentResolver: ContentResolver,pref:String, defaultVal : Boolean = false) : Boolean{
        val c = contentResolver.query(getBooleanUri(pref, defaultVal),null,null,null,null)
        if ( c != null && c.moveToFirst() ){
            return c.getString(c.getColumnIndex(pref)).toBoolean()
        }
        c.close()
        return defaultVal
    }

    fun getFloat(contentResolver: ContentResolver,pref:String, defaultVal : Float = 0f) : Float{
        val c = contentResolver.query(getFloatUri(pref, defaultVal),null,null,null,null)
        if ( c != null && c.moveToFirst() ){
            return c.getFloat(c.getColumnIndex(pref))
        }
        c.close()
        return defaultVal
    }

    fun setFloat(contentResolver: ContentResolver,pref: String, value : Float ){
        val contentValue = ContentValues()
        contentValue.put(pref, value)
        contentResolver.insert(getFloatUri(pref), contentValue)
    }

    fun getLong(contentResolver: ContentResolver,pref:String, defaultVal : Long = 0L) : Long{
        val c = contentResolver.query(getLongUri(pref, defaultVal),null,null,null,null)
        if ( c != null && c.moveToFirst() ){
            return c.getLong(c.getColumnIndex(pref))
        }
        c.close()
        return defaultVal
    }

    fun setLong(contentResolver: ContentResolver,pref: String, value : Long ){
        val contentValue = ContentValues()
        contentValue.put(pref, value)
        contentResolver.insert(getLongUri(pref), contentValue)
    }

    fun setBoolean(contentResolver: ContentResolver,pref: String, value : Boolean ){
        val contentValue = ContentValues()
        contentValue.put(pref, value)
        contentResolver.insert(getBooleanUri(pref), contentValue)
    }

    fun getStringSet(contentResolver: ContentResolver,pref: String, defaultVal : MutableSet<String> = mutableSetOf()) : MutableSet<String>{
        val c = contentResolver.query(getStringSetUri(pref), null,null,null,null)
        if ( c != null && c.moveToFirst() ){
            return mutableSetOf(c.getString(c.getColumnIndex(pref)))
        }
        c.close()
        return defaultVal
    }

    fun setStringSet(contentResolver: ContentResolver,pref:String, value : MutableSet<String>){
        val contentValue = ContentValues()
        contentValue.put(pref,value.toString())
        contentResolver.insert(getStringSetUri(pref), contentValue)
    }

    fun remove(contentResolver: ContentResolver,pref:String) = contentResolver.delete(getRemoveUri(pref), null, null)

    fun clear(contentResolver: ContentResolver) = contentResolver.delete(getClearUri(), null, null)
}