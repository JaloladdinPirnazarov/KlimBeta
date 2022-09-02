package com.jaloladdin.klimbeta

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.jaloladdin.klimbeta.DB.MySharedPreferences
import com.jaloladdin.klimbeta.Objects.Constants
import com.jaloladdin.klimbeta.Others.HelperClass
import com.jaloladdin.klimbeta.Others.VolleySingleton
import com.jaloladdin.klimbeta.databinding.ActivityDialogBinding
import org.json.JSONObject

class Dialog : AppCompatActivity() {
    private lateinit var binding: ActivityDialogBinding
    private lateinit var pref: MySharedPreferences
    private var functions = HelperClass()

    private var orderingPoints = 0
    private var walletName = ""
    private var walletPath = ""
    private var walletCurrency = Constants.NOT_SELECTED
    private var orderingMoney = 0
    private var comment = " "
    private var errorText = ""
    private var walletImage = 0
    private var eWallet = false
    private var withdravalText = ""
    private var ordered = false

    private var correct = true

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = MySharedPreferences(getSharedPreferences("MyPref", MODE_PRIVATE))
        onClickListeners()
        setVariables()
        setActivity()
    }

    private fun setVariables(){
        walletName = intent.getStringExtra(Constants.INTENT_WALLET_NAME)!!
        orderingMoney = pref.getPoints()
        when(walletName){
            Constants.PAYEER ->{
                withdravalText = getString(R.string.withdrawal_to_payeer)
                walletImage = R.drawable.payeer
                eWallet = true
            }

            Constants.QIWI ->{
                withdravalText = getString(R.string.withdrawal_to_qiwi)
                walletImage = R.drawable.qiwi
                eWallet = true
            }

            Constants.UZCARD ->{
                withdravalText = getString(R.string.withdrawal_to_uzcard)
                walletImage = R.drawable.uzcard }

            Constants.HUMO ->{
                withdravalText = getString(R.string.withdrawal_to_humo)
                walletImage = R.drawable.humo }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun onClickListeners(){

        binding.btnCancel.setOnClickListener { cancel() }
        binding.btnDone.setOnClickListener { checkRequisites() }
        binding.btnEdit.setOnClickListener { edit() }
        binding.btnOrder.setOnClickListener {
            if (ordered){
                Toast.makeText(this,getString(R.string.pleace_wait),Toast.LENGTH_SHORT).show()
            }else{
                binding.progressBar.visibility = View.VISIBLE
                ordered = true
                post()
            }

        }

        binding.radioGroup.setOnCheckedChangeListener{ _, checkedId->
            when (checkedId) {
                R.id.radioBtnUSD -> { walletCurrency = "USD" }
                R.id.radioBtnEUR -> { walletCurrency = "EUR" }
                R.id.radioBtnRUB -> { walletCurrency = "RUB" }
            }
        }
    }

    private fun cancel(){
        walletName = ""
        walletPath = ""
        walletCurrency = Constants.NOT_SELECTED
        orderingPoints = 0
        orderingMoney = 0
        comment = " "
        correct = true

        binding.edWalletPath.setText("")
        binding.edComment.setText("")
        binding.radioGroup.clearCheck()

        finish()
    }

    private fun setWithdrawalDialog() {
        val pathText: String

        if (eWallet){
            pathText = getString(R.string.type_wallet_path)
            binding.tvDialogChooseCurrency.visibility = View.VISIBLE
            binding.radioGroup.visibility = View.VISIBLE
        }
        else{
            pathText = getString(R.string.type_card_number)
            binding.tvDialogChooseCurrency.visibility = View.GONE
            binding.radioGroup.visibility = View.GONE
        }
        binding.constraintWithdrawalDialog.visibility = View.VISIBLE

        binding.tvDialogWalletName.text = withdravalText
        binding.textView63.text = pathText
        binding.imgWithdrawalDialogWallet.setImageResource(walletImage)
    }

    private fun checkRequisites(){
        walletPath = binding.edWalletPath.text.toString()
        comment = binding.edComment.text.toString()
        correct = true

        if (eWallet && walletCurrency == Constants.NOT_SELECTED){
            correct = false
            toastMessageCreator(getString(R.string.chooseWalletCurrency))
        }

        if (walletPath.isEmpty()){
            correct = false
            if (binding.radioGroup.isVisible){
                toastMessageCreator(getString(R.string.type_wallet_path))
            } else{
                toastMessageCreator(getString(R.string.type_card_number))
            }
        }

        if (correct){
            setCheckDialog()
        }
    }

    private fun setCheckDialog(){
        binding.constraintWithdrawalDialog.visibility = View.GONE
        binding.constraintCheckWithdrawalDialog.visibility = View.VISIBLE

        val tvWalletPathText:String
        var tvWalletCurrencyText = ""
        val tvOrderingMoneyText = "${getString(R.string.sum)}: ${functions.getBalance(orderingMoney)}$"
        val tvCommentText = "${getString(R.string.comment)}: $comment"

        if (eWallet){
            tvWalletPathText = "${getString(R.string.wallet_path)}: $walletPath"
            tvWalletCurrencyText = "${getString(R.string.wallet_currency)}: $walletCurrency"
            binding.tvDialogChooseCurrency.visibility = View.VISIBLE
            binding.tvCheckWalletCurrency.visibility = View.VISIBLE
        }
        else{
            tvWalletPathText = "${getString(R.string.card_number)}: $walletPath"
            binding.tvDialogChooseCurrency.visibility = View.GONE
            binding.tvCheckWalletCurrency.visibility = View.GONE
        }

        if (comment.isEmpty()){ binding.tvCheckComment.visibility = View.GONE ; comment = getString(R.string.no)}
        else{ binding.tvCheckComment.visibility = View.VISIBLE }

        binding.imgCheckWallet.setImageResource(walletImage)
        binding.tvCheckWalletName.text = withdravalText
        binding.tvCheckWalletPath.text = tvWalletPathText
        binding.tvCheckWalletCurrency.text = tvWalletCurrencyText
        binding.tvCheckMoney.text = tvOrderingMoneyText
        binding.tvCheckComment.text = tvCommentText
    }

    private fun edit(){
        binding.constraintCheckWithdrawalDialog.visibility = View.GONE
        binding.constraintWithdrawalDialog.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun post() {
        comment = HelperClass().setComment(comment)
        var walletId = ""
        when(walletName){
            Constants.PAYEER->{ walletId = Constants.PAYERR_ID }
            Constants.QIWI->{ walletId = Constants.QIWI_ID }
            Constants.UZCARD->{ walletId = Constants.UZCARD_ID }
            Constants.HUMO->{ walletId = Constants.HUMO_ID }
        }

        val params = HashMap<String,Any>()
        params["walletId"] = walletId
        params["summa"] = orderingMoney
        params["walletPath"] = walletPath.replace(" ","")
        params["walletCurrency"] = walletCurrency
        params["comment"] = comment

        val jsonObject = JSONObject(params.toString())

        val request = JsonObjectRequest(
            Request.Method.POST,Constants.POST_A_ORDER,jsonObject,
            {
                    response ->
                val success = response.getBoolean("success")
                if (success){
                    pref.savePoints(0)
                }
                saveOrderDetails(success)

            }, {
                errorText = it.toString()
                toastMessageCreator(getString(R.string.error))
                saveOrderDetails(false)
            })

        VolleySingleton.getInstance(this).addToRequestQueue(request)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun saveOrderDetails(b:Boolean){
        var title = ""
        var message = ""
        if(b){
            title = getString(R.string.successfully)
            message = getString(R.string.post_success)
        }
        else{
            title = getString(R.string.error)
            message = if (HelperClass().isOnline(this)){ "${getString(R.string.cause)}: $errorText" }
            else{ "${getString(R.string.cause)}: ${getString(R.string.no_internet_connection)}" }
        }
        pref.saveIsOrdered(true)
        pref.saveTitle(title)
        pref.saveMessage(message)
        finish()
    }

    private fun toastMessageCreator(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    private fun setActivity(){
        binding.constraintCheckWithdrawalDialog.visibility = View.GONE
        binding.constraintWithdrawalDialog.visibility = View.VISIBLE
        binding.imgWithdrawalDialogWallet.setImageResource(walletImage)
        binding.tvDialogWalletName.text = withdravalText
        if (eWallet){
            binding.tvDialogChooseCurrency.visibility = View.VISIBLE
            binding.radioGroup.visibility = View.VISIBLE
            binding.textView63.text = getString(R.string.type_wallet_path)
        }else{
            binding.tvDialogChooseCurrency.visibility = View.GONE
            binding.radioGroup.visibility = View.GONE
            binding.textView63.text = getString(R.string.type_card_number)
        }
        setWithdrawalDialog()
    }
}