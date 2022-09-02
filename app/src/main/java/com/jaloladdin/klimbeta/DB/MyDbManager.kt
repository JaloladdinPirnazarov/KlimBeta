package com.jaloladdin.klimbeta.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.jaloladdin.klimbeta.DataClasses.UserItems
import com.jaloladdin.klimbeta.DataClasses.WalletItems

class MyDbManager (private val context: Context){
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase
    }

    fun closeDb(){
        db?.close()
    }

    fun insertToUser(userItems:UserItems){
        val values = ContentValues().apply {
            put(MyDbName.COLUMN_NAME_USER_ID,userItems.user_id)
            put(MyDbName.COLUMN_NAME_USER_NAME,userItems.user_name)
            put(MyDbName.COLUMN_NAME_FULL_NAME,userItems.full_name)
            put(MyDbName.COLUMN_NAME_ALL_SHOWED_AD_COUNT,userItems.all_showed_add_count)
            put(MyDbName.COLUMN_NAME_POINTS,userItems.points)
        }
        db?.insert(MyDbName.TABLE_NAME_USER,null,values)
    }

    fun insertToWallets(walletItems:WalletItems){
        val values = ContentValues().apply {
            put(MyDbName.COLUMN_NAME_WALLET_ID,walletItems.wallet_id)
            put(MyDbName.COLUMN_NAME_WALLET_NAME,walletItems.wallet_name)
            put(MyDbName.COLUMN_NAME_WALLET_PATH,walletItems.wallet_path)
        }
    }

    fun updateUser(userItems: UserItems){
        val selection = "${BaseColumns._ID}=0"
        val values = ContentValues().apply {
            put(MyDbName.COLUMN_NAME_USER_ID,userItems.user_id)
            put(MyDbName.COLUMN_NAME_USER_NAME,userItems.user_name)
            put(MyDbName.COLUMN_NAME_FULL_NAME,userItems.full_name)
            put(MyDbName.COLUMN_NAME_ALL_SHOWED_AD_COUNT,userItems.all_showed_add_count)
            put(MyDbName.COLUMN_NAME_POINTS,userItems.points)
        }
        db?.update(MyDbName.TABLE_NAME_USER,values,selection,null)
    }
    fun readDataFromUser():ArrayList<UserItems>{
        val arrayList = ArrayList<UserItems>()
        val cursor = db?.query(MyDbName.TABLE_NAME_USER,null,null,null,null,null,null)
        while (cursor?.moveToNext()!!){
            val userItems = UserItems()

            var index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_USER_ID)
            userItems.user_id = cursor.getInt(index)

            index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_USER_NAME)
            userItems.user_name = cursor.getString(index)

            index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_FULL_NAME)
            userItems.full_name = cursor.getString(index)

            index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_ALL_SHOWED_AD_COUNT)
            userItems.all_showed_add_count = cursor.getInt(index)

            index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_MONTHLY_SHOWED_AD_COUNT)
            userItems.monthly_showed_ad_count = cursor.getInt(index)

            index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_POINTS)
            userItems.points = cursor.getInt(index)

            arrayList.add(userItems)
        }
        cursor.close()
        return arrayList
    }

    fun readDataFromWallets():WalletItems{
        val walletItems = WalletItems()
        val cursor = db?.query(MyDbName.TABLE_NAME_WALLETS,null,null,null,null,null,null)
        while (cursor?.moveToNext()!!){
            var index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_WALLET_ID)
            walletItems.wallet_id = cursor.getInt(index)

            index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_WALLET_NAME)
            walletItems.wallet_name = cursor.getString(index)

            index = cursor.getColumnIndex(MyDbName.COLUMN_NAME_WALLET_PATH)
            walletItems.wallet_path = cursor.getString(index)
        }
        cursor.close()
        return walletItems
    }
}