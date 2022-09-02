package com.jaloladdin.klimbeta

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.jaloladdin.klimbeta.DB.MySharedPreferences
import com.jaloladdin.klimbeta.DB.RcViewAdapterOrders
import com.jaloladdin.klimbeta.DataClasses.OrderItems
import com.jaloladdin.klimbeta.Objects.Constants
import com.jaloladdin.klimbeta.Others.HelperClass
import com.jaloladdin.klimbeta.databinding.ActivityAdminBinding
import org.json.JSONObject

class Admin : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private val myAdapter = RcViewAdapterOrders(this, ArrayList())
    private lateinit var pref: MySharedPreferences
    private val functions = HelperClass()

    private var pointsIsVisible = false
    private var statisticsIsVisible = false

    private var state = 0
    private var wallet = "0"
    private var url = Constants.GET_NOT_PAYED_ORDERS

    var allOrders = ArrayList<OrderItems>()
    var payeerOrders = ArrayList<OrderItems>()
    var qiwiOrders = ArrayList<OrderItems>()
    var uzcardOrders = ArrayList<OrderItems>()
    var humoOrders = ArrayList<OrderItems>()

    private var totalSum = 0
    private var payerOrdersSum = 0
    private var qiwiOrdersSum = 0
    private var uzcardOrdersSum = 0
    private var humoOrdersSum = 0

    private var admin = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = MySharedPreferences(getSharedPreferences("MyPref", MODE_PRIVATE))
        setPoints()
        onClickListeners()
        initRcView()
        getOrders()
        fillAdapter()
    }

    override fun onResume() {
        super.onResume()
        setActivity()
    }

    private fun checkAdmin(){
        when(binding.edAdmin.text.toString()){
            Constants.PASSWORD_JALOLADDIN->{
                binding.tvAdminName.text = "JALOLADDIN"
                binding.constraintAdmin.visibility = View.GONE
                binding.constraintAdminItems.visibility = View.VISIBLE
                binding.constraintPointsParent.visibility = View.VISIBLE
                binding.constraintStatisticsParent.visibility = View.VISIBLE
                binding.edAdmin.setText("")
                admin = 0
            }

            Constants.PASSWORD_ADMIN ->{
                binding.tvAdminName.text = "ADMIN"
                binding.constraintAdmin.visibility = View.GONE
                binding.constraintAdminItems.visibility = View.VISIBLE
                binding.edAdmin.setText("")
                admin = 0
            }

            else ->{ finish() }
        }
    }

    private fun addPoints(){
        val p = binding.edAdminAddPoints.text.toString()
        if (p.isEmpty()){ Toast.makeText(this,getString(R.string.error),Toast.LENGTH_SHORT).show()}
        else{ pref.savePoints(p.toInt()) ; setPoints() }
        binding.edAdminAddPoints.setText("")
    }

    private fun setPoints(){
        val p = "${functions.getBalance(pref.getPoints())} $"
        binding.tvAdminPoints.text = p
    }

    private fun initRcView(){
        binding.rcViewOrders2.layoutManager = LinearLayoutManager(this)
        binding.rcViewOrders2.adapter = myAdapter
    }

    private fun fillAdapter(){
        val orders = ArrayList<OrderItems>()
        when(wallet){
            "0"->{ orders.addAll(payeerOrders) }
            "1"->{ orders.addAll(qiwiOrders) }
            "2"->{ orders.addAll(uzcardOrders) }
            "3"->{ orders.addAll(humoOrders) }
            "4"->{ orders.addAll(allOrders) }
        }
        if (orders.size == 0){ binding.tvEmpty.visibility = View.VISIBLE }
        else{ binding.tvEmpty.visibility = View.GONE }
        myAdapter.updateAdapter(orders)
    }

    private fun getOrders(){
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET,url,
            {response -> parseJson(response)},
            {Toast.makeText(this,"Error: $it",Toast.LENGTH_SHORT).show()})
        queue.add(stringRequest)
    }

    private fun parseJson(response: String) {
        allOrders.clear()
        payeerOrders.clear()
        qiwiOrders.clear()
        uzcardOrders.clear()
        humoOrders.clear()

        totalSum = 0
        payerOrdersSum = 0
        qiwiOrdersSum = 0
        uzcardOrdersSum = 0
        humoOrdersSum = 0

        val jsonArray = JSONObject(response).getJSONArray("data")
        for (index in 0 until  jsonArray.length()){
            val item = OrderItems()
            val jsonObject = JSONObject(jsonArray[index].toString())
            item.order_id = jsonObject.getString("_id")
            item.order_state = jsonObject.getInt("state")
            item.wallet_name = jsonObject.getJSONObject("walletId").getString("name")
            item.wallet_currency = jsonObject.getString("walletCurrency")
            item.wallet_path = jsonObject.getString("walletPath")
            item.sum = jsonObject.getInt("summa")
            item.comment = jsonObject.getString("comment")
            item.order_created_date = jsonObject.getString("createdAt")
            allOrders.add(item)
            totalSum += item.sum
            when(item.wallet_name){
                Constants.PAYEER ->{
                    payeerOrders.add(item)
                    payerOrdersSum += item.sum
                }
                Constants.QIWI ->{
                    qiwiOrders.add(item)
                    qiwiOrdersSum += item.sum
                }
                Constants.UZCARD ->{
                    uzcardOrders.add(item)
                    uzcardOrdersSum += item.sum
                }
                Constants.HUMO ->{
                    humoOrders.add(item)
                    humoOrdersSum += item.sum
                }
            }
        }
        binding.tvAllOrdersCount.text = allOrders.size.toString()
        setStatistics()
        fillAdapter()
    }

    private fun setStatistics(){
        binding.apply {
            tvPayeerOrdersCount.text = payeerOrders.size.toString()
            tvQiwiOrdersCount.text = qiwiOrders.size.toString()
            tvUzcardOrdersCount.text = uzcardOrders.size.toString()
            tvHumoOrdersCount.text = humoOrders.size.toString()
            tvAllOrdersCount.text = allOrders.size.toString()
            var text = "${functions.getBalance(payerOrdersSum)}$"
            tvPayeerOrdersSum.text = text
            text = "${functions.getBalance(qiwiOrdersSum)}$"
            tvQiwiOrdersSum.text = text
            text = "${functions.getBalance(uzcardOrdersSum)}$"
            tvUzcardOrdersSum.text = text
            text = "${functions.getBalance(humoOrdersSum)}$"
            tvHumoOrdersSum.text = text
            text = "${functions.getBalance(totalSum)}$"
            tvAllOrdersSum.text = text
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onClickListeners(){
        binding.btnAdmin.setOnClickListener {
            admin ++
            if (admin == 4){
                checkAdmin()
            }
        }

        binding.btnAdminAddPoints.setOnClickListener { addPoints() }
        binding.imgShowHidePoints.setOnClickListener { showHidePoints() }
        binding.imgShowHideStatistics.setOnClickListener { showHideStatistics() }

        binding.tvNotPayed.setOnClickListener { state = 0 ;setTextViews(binding.tvNotPayed, binding.tvPayed, binding.tvAll) }
        binding.tvPayed.setOnClickListener { state = 1 ; setTextViews(binding.tvPayed, binding.tvNotPayed, binding.tvAll) }
        binding.tvAll.setOnClickListener { state = 2 ; setTextViews(binding.tvAll, binding.tvNotPayed, binding.tvPayed) }

        tabOnClickListener()
    }

    private fun setActivity(){
        binding.apply {
            constraintPointsParent.visibility = View.GONE
            constraintStatisticsParent.visibility = View.GONE
            constraintAdminItems.visibility = View.GONE
            constraintPoints.visibility = View.GONE
            connstraintStatistics.visibility = View.GONE
            constraintAdmin.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NewApi")
    private fun setTextViews(tView1:TextView, tView2:TextView, tView3:TextView){
        tView1.setBackgroundResource(R.drawable.text_view_choosed)
        tView1.setTextColor(getColor(R.color.black))
        tView2.setBackgroundResource(R.drawable.text_view_normal)
        tView2.setTextColor(getColor(R.color.white))
        tView3.setBackgroundResource(R.drawable.text_view_normal)
        tView3.setTextColor(getColor(R.color.white))
        Toast.makeText(this,"state:$state",Toast.LENGTH_SHORT).show()

        when(state){
            0 ->{ url = Constants.GET_NOT_PAYED_ORDERS }
            1 ->{ url = Constants.GET_PAYED_ORDERS }
            2 ->{ url = Constants.GET_ALL_ORDERS }
        }
        getOrders()
    }

    private fun tabOnClickListener(){
        binding.tabLayoutOrders.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                wallet = tab?.position.toString()
                Toast.makeText(this@Admin,wallet,Toast.LENGTH_SHORT).show()
                fillAdapter()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showHidePoints(){
        if (pointsIsVisible){
            binding.constraintPoints.visibility = View.GONE
            binding.imgShowHidePoints.setImageResource(R.drawable.ic_arrow_circle_down)
            pointsIsVisible = false
        }else{
            binding.constraintPoints.visibility = View.VISIBLE
            binding.imgShowHidePoints.setImageResource(R.drawable.ic_arrow_circle_up)
            pointsIsVisible = true
        }
    }

    private fun showHideStatistics(){
        if (statisticsIsVisible){
            binding.connstraintStatistics.visibility = View.GONE
            binding.imgShowHideStatistics.setImageResource(R.drawable.ic_arrow_circle_down)
            statisticsIsVisible = false
        }else{
            binding.connstraintStatistics.visibility = View.VISIBLE
            binding.imgShowHideStatistics.setImageResource(R.drawable.ic_arrow_circle_up)
            statisticsIsVisible = true
        }
    }

}