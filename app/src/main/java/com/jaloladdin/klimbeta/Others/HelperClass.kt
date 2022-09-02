package com.jaloladdin.klimbeta.Others

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.jaloladdin.klimbeta.R
import java.math.BigDecimal

class HelperClass() {

    fun copyClipBoard(text:String,context: Context){
        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("nonsense_data",text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context,context.getString(R.string.copied),Toast.LENGTH_SHORT).show()
    }

    fun setComment(text:String):String{
        var returnComment = ""

        for(item in text){
            returnComment += if (item == ' '){ '_' }else{ item }
        }

        return returnComment
    }

    fun isNumeric(value:Char):Boolean{
        return value == '0' || value == '1' || value == '2' || value == '3' || value == '4' || value == '5' || value == '6' || value == '7' || value == '8' || value == '9' || value == '.'
    }

    @RequiresApi(Build.VERSION_CODES.M)
     fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun getBalance(points:Int):String{
        val balance = (points.toDouble() / 10000).toString()
        return BigDecimal(balance).toPlainString()
    }

}