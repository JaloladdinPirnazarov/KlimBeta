package com.jaloladdin.klimbeta.DB

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.jaloladdin.klimbeta.DataClasses.OrderItems
import com.jaloladdin.klimbeta.Others.HelperClass
import com.jaloladdin.klimbeta.MainActivity
import com.jaloladdin.klimbeta.Objects.Constants
import com.jaloladdin.klimbeta.R
import com.jaloladdin.klimbeta.Others.VolleySingleton
import org.json.JSONObject

class RcViewAdapterOrders(private val context: Context,private var listArray: ArrayList<OrderItems>): RecyclerView.Adapter<RcViewAdapterOrders.MyHolder>() {
    val functions = HelperClass()


    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvRcWalletName: TextView = itemView.findViewById(R.id.tvRcWalletName)
        val tvRcWalletCurrency = itemView.findViewById<TextView>(R.id.tvRcWalletCurrency)
        val tvRcWalletPath = itemView.findViewById<TextView>(R.id.tvRcWalletPath)
        val tvRcSum = itemView.findViewById<TextView>(R.id.tvRcSum)
        val tvRcCreatedAt = itemView.findViewById<TextView>(R.id.tvRcCreatedAt)
        val tvRcComment = itemView.findViewById<TextView>(R.id.tvRcComment)
        val tvRcOrderState = itemView.findViewById<TextView>(R.id.tvRcOrderState)
        val btnMakePayed = itemView.findViewById<Button>(R.id.btnMakePayed)
        val btnCopy = itemView.findViewById<ImageView>(R.id.btnCopy)
        val imgRcWallet = itemView.findViewById<ImageView>(R.id.imgRcWallet)


        fun setData(item: OrderItems, context: Context, functions: HelperClass){
            var text = ""
            tvRcWalletName.text = item.wallet_name
            val img = when(item.wallet_name){
                Constants.PAYEER->{ R.drawable.payeer }
                Constants.QIWI->{ R.drawable.qiwi }
                Constants.UZCARD->{ R.drawable.uzcard }
                Constants.HUMO->{ R.drawable.humo }
                else->{ R.drawable.payeer }
            }
            imgRcWallet.setImageResource(img)
            if (item.wallet_currency == Constants.NOT_SELECTED){ tvRcWalletCurrency.visibility = View.GONE }
            else{ tvRcWalletCurrency.visibility = View.VISIBLE ; text = "${context.getString(R.string.wallet_currency)}: ${item.wallet_currency}" ;tvRcWalletCurrency.text = text }
            tvRcWalletPath.text = "${context.getString(R.string.number)}: ${item.wallet_path}"
            text = "${context.getString(R.string.sum)}: ${functions.getBalance(item.sum)} $"
            tvRcSum.text = text
            tvRcCreatedAt.text = item.order_created_date.substringBefore('T')
            if (item.comment == "" || item.comment == "null"){ tvRcComment.visibility = View.GONE }
            else{ tvRcComment.visibility = View.VISIBLE ; text = "${context.getString(R.string.comment)}: ${item.comment}" ;tvRcComment.text =  text}
            if (item.order_state == 0){
                btnMakePayed.visibility = View.VISIBLE
                tvRcOrderState.setTextColor(context.resources.getColor(R.color.wrong_answer_box_color))
                tvRcOrderState.text = context.getString(R.string.not_payed)
            }else if (item.order_state == 1){
                btnMakePayed.visibility = View.GONE
                tvRcOrderState.setTextColor(context.resources.getColor(R.color.correct_answer_box_color))
                tvRcOrderState.text = context.getString(R.string.payed)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_order_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = listArray[position]
        holder.setData(item,context,functions)
        holder.btnCopy.setOnClickListener { functions.copyClipBoard(item.wallet_path,context) }
        holder.btnMakePayed.setOnClickListener { put(item.order_id,position) }
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listItems: List<OrderItems>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    private fun put(orderId:String,position: Int){
        val url = Constants.PUT_STATE + orderId
        val params = HashMap<String,Int>()
        params["state"] = 1
        val jsonObject = JSONObject(params.toString())
        val request = JsonObjectRequest(
            Request.Method.PUT,url,jsonObject,
            {response->
                val success = response.getBoolean("success")
                if (success){
                    Toast.makeText(context,"ГОТОВО", Toast.LENGTH_SHORT).show()
                    removeItem(position)
                }
                else{ Toast.makeText(context,response.toString(), Toast.LENGTH_SHORT).show() }
            },
            { MainActivity().toastMessageCreator(it.toString()) })

        VolleySingleton.getInstance(context).addToRequestQueue(request)
    }

    private fun removeItem(position: Int){
        listArray.removeAt(position)
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(position)
    }
}