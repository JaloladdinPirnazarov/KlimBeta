package com.jaloladdin.klimbeta.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, MyDbName.DATABASE_NAME, null, MyDbName.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(MyDbName.CREATE_TABLE_USER)
        db?.execSQL(MyDbName.CREATE_TABLE_WALLETS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(MyDbName.DROP_TABLE_USER)
        db?.execSQL(MyDbName.DROP_TABLE_WALLETS)
        onCreate(db)
    }
}