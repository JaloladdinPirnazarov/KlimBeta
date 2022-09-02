package com.jaloladdin.klimbeta.DB

import android.provider.BaseColumns

object MyDbName {
    const val TABLE_NAME_USER = "table_name_user"
    const val COLUMN_NAME_USER_ID = "user_id"
    const val COLUMN_NAME_USER_NAME = "user_name"
    const val COLUMN_NAME_FULL_NAME = "full_name"
    const val COLUMN_NAME_ALL_SHOWED_AD_COUNT = "all_showed_ad_count"
    const val COLUMN_NAME_MONTHLY_SHOWED_AD_COUNT = "monthly_showed_ad_count"
    const val COLUMN_NAME_POINTS = "points"

    const val TABLE_NAME_WALLETS = "wallets"
    const val COLUMN_NAME_WALLET_ID = "wallet_id"
    const val COLUMN_NAME_WALLET_NAME = "wallet_name"
    const val COLUMN_NAME_WALLET_PATH = "wallet_path"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "MyDataBase.db"

    const val CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS $TABLE_NAME_USER (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_USER_ID INTEGER, $COLUMN_NAME_USER_NAME TEXT, " +
            "$COLUMN_NAME_FULL_NAME TEXT, $COLUMN_NAME_ALL_SHOWED_AD_COUNT INTEGER, " +
            "$COLUMN_NAME_MONTHLY_SHOWED_AD_COUNT INTEGER, $COLUMN_NAME_POINTS INTEGER)"

    const val CREATE_TABLE_WALLETS = "CREATE TABLE IF NOT EXISTS $TABLE_NAME_WALLETS (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_WALLET_ID INTEGER, " +
            "$COLUMN_NAME_WALLET_ID INTEGER, $COLUMN_NAME_WALLET_NAME TEXT, $COLUMN_NAME_WALLET_PATH TEXT)"

    const val DROP_TABLE_USER = "DROP TABLE IF EXISTS $TABLE_NAME_USER"

    const val DROP_TABLE_WALLETS = "DROP TABLE IF EXISTS $TABLE_NAME_WALLETS"
}