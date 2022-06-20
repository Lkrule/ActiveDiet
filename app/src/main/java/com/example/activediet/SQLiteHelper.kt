package com.example.activediet;

import android.content.ContentValues
import android.content.Context;
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class SQLiteHelper (context:Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "excercise.db"
        private const val TBL_EXCERCISE = "tbl_excercise"
        private const val ID = "id"
        private const val NAME = "name"
        private const val DETAILS = "details"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTblExcercise = ("CREATE TABLE " + TBL_EXCERCISE + "(" + ID + " INTEGER PRIMARY KEY,"
                + NAME + " TEXT," + DETAILS + " TEXT" + ")")
        db?.execSQL(createTblExcercise)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_EXCERCISE")
        onCreate(db)
    }
    fun insertExcercise(std: ExcerciseModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(DETAILS, std.details)

        val success = db.insert(TBL_EXCERCISE, null, contentValues)
        db.close()
        return success
    }
    fun getAllExcercises(): ArrayList<ExcerciseModel>{
        val stdList: ArrayList<ExcerciseModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_EXCERCISE"
        val db= this.readableDatabase
        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e:Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var name:String
        var details:String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                details = cursor.getString(cursor.getColumnIndex("details"))

                val std = ExcerciseModel(id = id, name = name, details = details)
                stdList.add(std)
            }while(cursor.moveToNext())
        }
        return stdList
    }
    fun updateExercise(std: ExcerciseModel): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,std.id)
        contentValues.put(NAME,std.name)
        contentValues.put(DETAILS,std.details)
        val success = db.update(TBL_EXCERCISE, contentValues,"id="+std.id, null)
        db.close()
        return success
    }
    fun deleteExerciseById(id:Int): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)
        val success = db.delete(TBL_EXCERCISE, "id=$id", null)
        db.close()
        return success
    }
}

