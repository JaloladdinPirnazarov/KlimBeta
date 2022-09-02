package com.jaloladdin.klimbeta

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jaloladdin.klimbeta.DB.MySharedPreferences
import com.jaloladdin.klimbeta.Fragments.WalletDialog
import com.jaloladdin.klimbeta.Objects.Constants
import com.jaloladdin.klimbeta.Others.AppMainState
import com.jaloladdin.klimbeta.Others.HelperClass
import com.jaloladdin.klimbeta.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var functions = HelperClass()
    private var correctAnswer = 0
    private var questionCount = 1
    private var allQuestionCount = 1
    private var answered = false
    private var interAd: InterstitialAd? = null
    private var tvQuestionClickedCount = 0
    private var addShowed = false
    private lateinit var pref:MySharedPreferences

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = MySharedPreferences(getSharedPreferences("MyPref", MODE_PRIVATE))
        onClickListeners()
        setActivity()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setActivity(){
        if (HelperClass().isOnline(this)){
            binding.ConstraintNoInternetConnection.visibility = View.GONE
            binding.mainContent.visibility = View.VISIBLE
            initAdMob()
            hideAnswerStatus()
            setQuestion()
            (application as AppMainState).showAdIfAvailable(this){}
            setAllData()
            loadInterAd()
        }else{
            binding.mainContent.visibility = View.GONE
            binding.ConstraintNoInternetConnection.visibility = View.VISIBLE
        }
    }

    private fun hideAnswerStatus(){
        binding.apply {
            answer1Status.visibility = View.INVISIBLE
            answer2Status.visibility = View.INVISIBLE
            answer3Status.visibility = View.INVISIBLE
            answer4Status.visibility = View.INVISIBLE
            answer5Status.visibility = View.INVISIBLE
            answer6Status.visibility = View.INVISIBLE
        }
    }

    private fun onClickListeners(){
        binding.answer1.setOnClickListener { checkAnswer(1) }
        binding.answer2.setOnClickListener { checkAnswer(2) }
        binding.tvNext.setOnClickListener {
            if(answered){
                setQuestion()
                questionCount++
                allQuestionCount++
            }
            else{
                toastMessageCreator(getString(R.string.choose))
            }
        }
        binding.constraintWithdrawal.setOnClickListener { showWithdrawalDialog() }
        binding.tvQuestion.setOnClickListener { tvQuestionClickedCount ++ ; if(tvQuestionClickedCount == 4){ startAdminActivity() } }

        binding.constraintComunity.setOnClickListener { openLink(Constants.COMMUNITY_LINK) }
        binding.constraintInstruction.setOnClickListener { openLink(Constants.VIDEO_INSTRUCTION_LINK) }
    }

    private fun checkAnswer(answer:Int){
        answered = true
        val answerBackGround :Int
        val answerStatusBackGround :Int
        if (answer != correctAnswer){
            answerBackGround = R.drawable.wrong_answer_background
            answerStatusBackGround = R.drawable.wrong_answer_status_background
        }else{
            answerBackGround = R.drawable.correct_answer_background
            answerStatusBackGround = R.drawable.correct_answer_status_background
        }

        if (answer == 1){
            binding.answer1.setBackgroundResource(answerBackGround)
        }else{
            binding.answer2.setBackgroundResource(answerBackGround)
        }

        when(questionCount){
            1->{ binding.answer1Status.visibility = View.VISIBLE
                binding.answer1Status.setBackgroundResource(answerStatusBackGround) }

            2->{ binding.answer2Status.visibility = View.VISIBLE
            binding.answer2Status.setBackgroundResource(answerStatusBackGround) }

            3->{ binding.answer3Status.visibility = View.VISIBLE
                binding.answer3Status.setBackgroundResource(answerStatusBackGround) }

            4->{ binding.answer4Status.visibility = View.VISIBLE
                binding.answer4Status.setBackgroundResource(answerStatusBackGround) }

            5->{ binding.answer5Status.visibility = View.VISIBLE
                binding.answer5Status.setBackgroundResource(answerStatusBackGround) }

            6->{ binding.answer6Status.visibility = View.VISIBLE
                binding.answer6Status.setBackgroundResource(answerStatusBackGround) }
        }
    }

    private fun setQuestion(){
        if (questionCount == 6){
            showInterAd()
            hideAnswerStatus()
            questionCount = 0
        }
        binding.answer1.setBackgroundResource(R.drawable.neutral_answer_status)
        binding.answer2.setBackgroundResource(R.drawable.neutral_answer_status)
        val num1 :Int
        val num2 :Int
        val question:String
        var answer = ""
        val answer1Text:String
        val answer2Text:String
        val divide:Boolean

        when {
            allQuestionCount%2==0 -> {
                num1 = randNumbers(1,800)
                num2 = randNumbers(0,800)
                question = "$num1 - $num2 = ?"
                answer = (num1 - num2).toString()
                divide = false
            }
            allQuestionCount%3==0 -> {
                num1 = randNumbers(17,800)
                num2 = randNumbers(1,num1)
                question = "$num1 / $num2 = ?"
                var dote = false
                var variableCount = 0
                for (item in (num1.toDouble() / num2.toDouble()).toString()){
                    if (variableCount == 2){ break }
                    if (dote){ variableCount++ }
                    if (item == '.'){ dote = true}
                    answer += item
                }
                divide = true
            }
            allQuestionCount%7==0 -> {
                num1 = randNumbers(8,45)
                num2 = randNumbers(0,11)
                question = "$num1 * $num2 = ?"
                answer = (num1 * num2).toString()
                divide = false
            }
            else -> {
                num1 = randNumbers(0,800)
                num2 = randNumbers(0,800)
                question = "$num1 + $num2 = ?"
                answer = (num1 + num2).toString()
                divide = false
            }
        }

        if (num1%2==0){
            answer1Text = answer
            answer2Text = if (divide){
                "${(answer.substringBefore('.')).toInt()+13}.${answer.substringAfter(".")}"
            } else{
                (answer.toInt() + 13).toString()
            }
            correctAnswer = 1

        }else{
            answer2Text = answer
            answer1Text = if (divide){
                "${(answer.substringBefore('.')).toInt()+13}.${answer.substringAfter(".")}"
            } else{
                (answer.toInt() + 13).toString()
            }
            correctAnswer = 2
        }
        binding.tvQuestion.text = question
        binding.tvAnswer1.text = answer1Text
        binding.tvAnswer2.text = answer2Text

    }

    private fun randNumbers(rStart:Int, rEnd:Int):Int{
        return (rStart..rEnd).random()
    }

    fun toastMessageCreator(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun initAdMob(){
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        val adRequest2 = AdRequest.Builder().build()
        binding.adView2.loadAd(adRequest2)

        val adRequest3 = AdRequest.Builder().build()
        binding.adView3.loadAd(adRequest3)

        val adRequest4 = AdRequest.Builder().build()
        binding.adView4.loadAd(adRequest4)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        binding.adView.resume()
        binding.adView2.resume()
        binding.adView3.resume()
        binding.adView4.resume()
        alertDialog()
        loadInterAd()
        setAllData()
    }

    override fun onPause() {
        super.onPause()
        binding.adView.pause()
        binding.adView2.pause()
        binding.adView3.pause()
        binding.adView4.pause()
        setAllData()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.adView.destroy()
        binding.adView2.destroy()
        binding.adView3.destroy()
        binding.adView4.destroy()
    }

    private fun loadInterAd(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,
            Constants.INTER_AD, adRequest,
            object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    interAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interAd = ad
                }
            })
    }

    private fun showInterAd(){
        if (interAd != null){
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
//                override fun onAdClicked() {
//                    super.onAdClicked()
//                    addClicked = true
//                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    interAd = null
                    loadInterAd()

                    if (addShowed){
                        increase()
                        addShowed = false
                        //addClicked = false
                    }

                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    interAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    loadInterAd()
                    addShowed = true
                }
            }
            interAd?.show(this)
        }else{
            loadInterAd()
        }
    }

    private fun openLink(urls: String) {
        val uris = Uri.parse(urls)
        val intents = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intents.putExtras(b)
        startActivity(intents)
    }

    private fun setAllData(){

        val balance = functions.getBalance(pref.getPoints())
        val txt = "$balance $"
        binding.tvPoints.text = txt
    }

    private fun increase(){
        pref.savePoints((pref.getPoints() + 4))
        setAllData()
        toastMessageCreator("+0.0004$")
    }

    private fun showWithdrawalDialog(){
        val dialog = WalletDialog(pref.getPoints())
        dialog.show(supportFragmentManager,"customDialog")
    }

    private fun startAdminActivity(){
        tvQuestionClickedCount = 0
        val i = Intent(this,Admin::class.java)
        startActivity(i)
    }

    private fun alertDialog(){
            if (pref.getIsOrdered()){
                val title = pref.getTitle()
                val message = pref.getMessage()

                val builder = AlertDialog.Builder(this)
                builder.setTitle(title)
                builder.setMessage(message)
                builder.setPositiveButton("ok") { _, _ -> }
                pref.saveIsOrdered(false)
                builder.show()
            }
        }
}
