package com.example.libraryrertrofitpro

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(val context: Context?, val name: String?, val version: Int) :
    SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL(
                "create table libraryTBL(" +
                        "code text primary key," +
                        "name text," +
                        "phone text ," +
                        "address text," +
                        "latitude text," +
                        "longitude text)")
        }
    } //onCreate
//lbrry_seq_no text primary key

    fun selectAll(): MutableList<LibraryData>? {
        val db: SQLiteDatabase = this.readableDatabase
        var cursor: Cursor? = null
        val mutableList: MutableList<LibraryData>? = mutableListOf()
        try {
            cursor = db.rawQuery("select * from libraryTBL", null)
            if (cursor.count >= 1) {
                Log.d("DBHelper", "selectAll 성공 ")
                while (cursor.moveToNext()) {
                    val libraryData = LibraryData(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                    )
                    mutableList?.add(libraryData)
                }
            }
        } catch (e: SQLException) {
            Log.d("DBHelper", "selectAll 예외 발생 ${e.printStackTrace()}")
        }
        return mutableList

    }
    fun insertTBL (data: LibraryData): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        var flag = false
        try {
            db.execSQL("insert into libraryTBL values(" +
                    "'${data.code}','${data.name}'" +
                    "'${data.phone}','${data.address}'" +
                    "'${data.latitude}','${data.longitude}'" +
                    ")")
            flag = true
        } catch (e: SQLException) {
            flag = false
        }
        return flag
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists member")
        onCreate(db)
    }
}