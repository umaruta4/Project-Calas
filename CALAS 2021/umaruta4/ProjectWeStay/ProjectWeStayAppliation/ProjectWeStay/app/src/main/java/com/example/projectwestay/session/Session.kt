package com.example.projectwestay.session

import android.content.Context

class Session {
    companion object {
        fun close(context: Context) {
            val sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        fun save(context: Context, key: String?, value: String?) {
            val sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun read(context: Context, key: String?, defaultValue: String?): String? {
            val sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, defaultValue)
        }
    }
}