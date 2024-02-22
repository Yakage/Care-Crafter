package com.carecrafter.sqlitedatabase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.RoomMasterTable.TABLE_NAME

class CareCrafterDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "CareCrafter.db"
        const val TABLE_NAME = "User"
        const val COLUMN_ID = "ID INTEGER PRIMARY KEY"
        const val COLUMN_USERID = "userID INTEGER"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID, $COLUMN_USERID)"
//        val contentValues = ContentValues().apply {
//            put("ID", 1)
//            put(COLUMN_USERID, null as Int?)
//        }
//        db.insert(TABLE_NAME, null, contentValues)
        db.execSQL(createTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


//    fun addOrUpdateToken(userId: Int) {
//        val CURRENT_ID = "ID"
//        val db = this.writableDatabase
//        try {
//         val values = ContentValues().apply {
//               put(COLUMN_USERID, userId)
//           }
//           db.update(TABLE_NAME, values, "$CURRENT_ID = ?", arrayOf("1"))
//        } finally {
//            db.close()
//        }
//    }


    @SuppressLint("Range")
    fun getUserID(): String? {
        val db = this.readableDatabase
        var userID: String? = null
        val query = "SELECT $COLUMN_USERID FROM $TABLE_NAME WHERE $COLUMN_ID = ?" // Adjusted to use a parameterized query
        val cursor = db.rawQuery(query, arrayOf("1")) // Assuming you want to retrieve the userID for the first row
        cursor.use { cursor ->
            if (cursor.moveToFirst()) {
                userID = cursor.getString(cursor.getColumnIndex(COLUMN_USERID))
            }
        }
        db.close()
        return userID
    }


    fun deleteAllUserData() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
}