package com.jaloladdin.klimbeta.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.jaloladdin.klimbeta.Dialog

import com.jaloladdin.klimbeta.Objects.Constants
import com.jaloladdin.klimbeta.Others.HelperClass
import com.jaloladdin.klimbeta.R
import com.jaloladdin.klimbeta.databinding.WalletDialogBinding



class WalletDialog(private val balance:Int) :DialogFragment() {
    lateinit var binding: WalletDialogBinding
    private var walletName = Constants.NOT_SELECTED
    private var enoughMoney = true
    private var functions = HelperClass()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  WalletDialogBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDialog()
        onClickListeners()
    }

    private fun setDialog(){

        val money = functions.getBalance(balance)
        val txt = "${getString(R.string.withdrawal)} $money $"

        binding.textView4.text = txt
        if (balance >= 3000){
            enoughMoney = true
            binding.textView7.visibility = View.GONE
        }else{
            binding.textView4.setTextColor(resources.getColor(R.color.wrong_answer_box_color))
            enoughMoney = false
            binding.textView7.visibility = View.VISIBLE
        }
    }

    private fun onClickListeners(){
        binding.btnCancelWithdrawal.setOnClickListener { dismiss() }
        binding.btnCountinue.setOnClickListener { continueWithdrawal() }
        binding.radioGroupWallets.setOnCheckedChangeListener { _, id ->
            when(id){
                R.id.radioPayeer ->{ walletName = Constants.PAYEER }
                R.id.radioQiwi ->{ walletName = Constants.QIWI }
                R.id.radioUzcard ->{ walletName = Constants.UZCARD }
                R.id.radioHumo ->{ walletName = Constants.HUMO }
            }
        }
    }

    private fun continueWithdrawal(){
        if (enoughMoney){
            if (walletName == Constants.NOT_SELECTED){ Toast.makeText(context,getString(R.string.choose_withdrawal_methood),Toast.LENGTH_SHORT).show() }
            else { startDialogActivity() }
        }else{ Toast.makeText(context,getString(R.string.dont_have_enough_points),Toast.LENGTH_SHORT).show() }
    }

    private fun startDialogActivity(){
        dismiss()
        val i = Intent(context,Dialog::class.java).putExtra(Constants.INTENT_WALLET_NAME,walletName)
        startActivity(i)
    }


}