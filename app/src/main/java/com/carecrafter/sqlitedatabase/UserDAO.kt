package com.carecrafter.sqlitedatabase

import android.content.ContentValues
import android.content.Context
import androidx.core.database.getStringOrNull

class UserDAO(context: Context) {
    private val dbHelper = CareCrafterDatabaseHelper(context)
    fun insertUser(user: User): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("Name", user.name)
            put("Email", user.email)
            put("Age", user.age)
            put("Height", user.height)
            put("Gender", user.gender)
            put("Weight", user.weight)
            put("Password", user.password)
            put("ConfirmPassword", user.confirmPassword)
            put("Role", user.role)
            put("Status", user.status)
        }

        return db.insert("User", null, values)
    }

    fun readUser(userId: Int): User? {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            "User",
            null,
            "UserID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            User(
                userId = cursor.getInt(cursor.getColumnIndex("UserID")),
                name = cursor.getStringOrNull(cursor.getColumnIndex("Name")),
                email = cursor.getStringOrNull(cursor.getColumnIndex("Email")),
                age = cursor.getInt(cursor.getColumnIndex("Age")),
                height = cursor.getFloat(cursor.getColumnIndex("Height")),
                gender = cursor.getStringOrNull(cursor.getColumnIndex("Gender")),
                weight = cursor.getFloat(cursor.getColumnIndex("Weight")),
                password = cursor.getStringOrNull(cursor.getColumnIndex("Password")),
                confirmPassword = cursor.getStringOrNull(cursor.getColumnIndex("ConfirmPassword")),
                role = cursor.getStringOrNull(cursor.getColumnIndex("Role")),
                status = cursor.getStringOrNull(cursor.getColumnIndex("Status"))
            )
        } else {
            null
        }
    }

    fun updateUser(user: User): Int {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("Name", user.name)
            put("Email", user.email)
            put("Age", user.age)
            put("Height", user.height)
            put("Gender", user.gender)
            put("Weight", user.weight)
            put("Password", user.password)
            put("ConfirmPassword", user.confirmPassword)
            put("Role", user.role)
            put("Status", user.status)
        }

        return db.update(
            "User",
            values,
            "UserID = ?",
            arrayOf(user.userId.toString())
        )
    }

    fun deleteUser(userId: Int): Int {
        val db = dbHelper.writableDatabase

        return db.delete(
            "User",
            "UserID = ?",
            arrayOf(userId.toString())
        )
    }
}