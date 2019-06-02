package com.example.sequencegame

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DATABASE_NAME = "MyDB"

val TABLE_USERS = "Users"

val COL_ID = "id"
val COL_NAME = "name"
val COL_PASSWORD  = "password"
val COL_CURSCORE = "curScore"
val COL_BESTSCORE = "bestScore"


val createTableUsers = "CREATE TABLE IF NOT EXISTS $TABLE_USERS(" +
        "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "$COL_NAME TEXT, " +
        "$COL_PASSWORD TEXT, " +
        "$COL_CURSCORE INTEGER, " +
        "$COL_BESTSCORE INTEGER)"


val dropTableUsers = "DROP TABLE IF EXISTS $TABLE_USERS"

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTableUsers)
        println("jestem")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(dropTableUsers)
        onCreate(db)
    }

    fun updateCurScore(name : String, score : Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_CURSCORE, score)
        val success = db.update(TABLE_USERS, values, COL_NAME + " = " + "\"$name\"", null)
        db.close()
    }

    fun updateBestScore(name : String, score : Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_BESTSCORE, score)
        val success = db.update(TABLE_USERS, values, COL_NAME + " = " + "\"$name\"", null)
        db.close()
    }
    fun insertUser(user: User) {
        val db = this.writableDatabase
        var value = ContentValues()
        value.put(COL_NAME, user.name)
        value.put(COL_PASSWORD, user.password)
        value.put(COL_CURSCORE, user.curScore)
        value.put(COL_BESTSCORE, user.bestScore)
        db.insert(TABLE_USERS, null,value)
        db.close()

    }

    fun readUsers() : ArrayList<User> {
        val db = this.readableDatabase

        var list : ArrayList<User> = ArrayList()

        var query = "SELECT * FROM " + TABLE_USERS
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val user = User(
                    result.getString(result.getColumnIndex(COL_ID)).toInt(),
                    result.getString(result.getColumnIndex(COL_NAME)),
                    result.getString(result.getColumnIndex(COL_PASSWORD)),
                    result.getString(result.getColumnIndex(COL_CURSCORE)).toInt(),
                    result.getString(result.getColumnIndex(COL_BESTSCORE)).toInt()
                )
                list.add(user)
            }while (result.moveToNext())
        }


        result.close()
        return list
    }

}