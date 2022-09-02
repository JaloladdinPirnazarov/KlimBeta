package com.jaloladdin.klimbeta.DB

import android.content.SharedPreferences
import com.jaloladdin.klimbeta.Objects.Constants

class MySharedPreferences(private val preferences: SharedPreferences) {
    private val editor = preferences.edit()


    fun savePoints(points:Int){
        editor.putInt(Constants.POINTS,points)
        editor.apply()
    }

    fun getPoints():Int{
        return preferences.getInt(Constants.POINTS,0)
    }

    fun saveIsOrdered(isOrdered:Boolean){
        editor.putBoolean(Constants.IS_ORDERED,isOrdered)
        editor.apply()
    }

    fun getIsOrdered():Boolean{
        return preferences.getBoolean(Constants.IS_ORDERED,false)
    }

    fun saveTitle(title:String){
        editor.putString(Constants.TITLE,title)
        editor.apply()
    }

    fun getTitle():String?{
        return preferences.getString(Constants.TITLE,".")
    }

    fun saveMessage(message:String){
        editor.putString(Constants.MESSAGE,message)
        editor.apply()
    }

    fun getMessage():String?{
        return preferences.getString(Constants.MESSAGE,".")
    }
}