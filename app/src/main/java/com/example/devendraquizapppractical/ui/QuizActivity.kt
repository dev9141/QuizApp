package com.example.devendraquizapppractical.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.devendraquizapppractical.R
import com.example.devendraquizapppractical.database_setup.DbRepo
import com.example.devendraquizapppractical.database_setup.QuizDatabaseHelper
import com.example.devendraquizapppractical.databinding.ActivityQuizBinding
import com.example.devendraquizapppractical.modal.QuestionModal

class QuizActivity : AppCompatActivity() {
    lateinit var binding: ActivityQuizBinding
    private val questionList: ArrayList<QuestionModal> = arrayListOf()
    private var question: QuestionModal? = null
    var countDownTimer: CountDownTimer? = null
    var questionIndex:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        binding.tvTotalScore.text = "Score: ${StartQuizActivity.totalScore.toString()}"
        getIntentQueData()
        binding.btnNextQuestion.setOnClickListener {
            countDownTimer?.cancel()
            countDownTimer = null
            Log.e("NextQueIndex: ", "questionIndex: $questionIndex")
            if(questionIndex < 9) {
                Log.e("NextQueIndex: ", "questionIndex: if")
                val rbSelectedOption = findViewById<RadioButton>(binding.rgOptions.checkedRadioButtonId)
                val ansIndex = binding.rgOptions.indexOfChild(rbSelectedOption)
                if(rbSelectedOption != null && rbSelectedOption.text == question?.answer){
                    val remainSecond = binding.tvTimer.text.toString().toInt()
                    StartQuizActivity.totalCorrectAns = StartQuizActivity.totalCorrectAns + 1
                    StartQuizActivity.totalScore = StartQuizActivity.totalScore + 10 + remainSecond
                }
                questionIndex++
                Log.e("NextQueIndex: ", "questionIndex after ++: $questionIndex")
                binding.rgOptions.clearCheck()
                setQuestion()
            }
            else{
                Log.e("NextQueIndex: ", "questionIndex: else")
                val rbSelectedOption = findViewById<RadioButton>(binding.rgOptions.checkedRadioButtonId)
                if(rbSelectedOption != null && rbSelectedOption.text == question?.answer){
                    val remainSecond = binding.tvTimer.text.toString().toInt()
                    StartQuizActivity.totalCorrectAns = StartQuizActivity.totalCorrectAns + 1
                    StartQuizActivity.totalScore = StartQuizActivity.totalScore + 10 + remainSecond
                }
                binding.rgOptions.clearCheck()

                startActivity(Intent(this, ResultActivity::class.java))
                finish()
            }
        }

        binding.optionOne.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.optionOne.isEnabled = false
            binding.optionOne.isClickable = false
            binding.optionTwo.isEnabled = false
            binding.optionTwo.isClickable = false
            binding.optionThree.isEnabled = false
            binding.optionThree.isClickable = false
            if(binding.optionOne.text == question?.answer) {
                binding.optionOne.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.btnNextQuestion.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            }
            else{
                binding.optionOne.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.btnNextQuestion.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            }
        }

        binding.optionTwo.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.optionOne.isEnabled = false
            binding.optionOne.isClickable = false
            binding.optionTwo.isEnabled = false
            binding.optionTwo.isClickable = false
            binding.optionThree.isEnabled = false
            binding.optionThree.isClickable = false
            if(binding.optionTwo.text == question?.answer) {
                binding.optionTwo.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.btnNextQuestion.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            }
            else{
                binding.optionTwo.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.btnNextQuestion.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            }
        }

        binding.optionThree.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.optionOne.isEnabled = false
            binding.optionOne.isClickable = false
            binding.optionTwo.isEnabled = false
            binding.optionTwo.isClickable = false
            binding.optionThree.isEnabled = false
            binding.optionThree.isClickable = false
            if(binding.optionThree.text == question?.answer) {
                binding.optionThree.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.btnNextQuestion.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            }
            else{
                binding.optionThree.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.btnNextQuestion.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getIntentQueData() {
        val intent = intent!!
        if (intent.hasExtra("QuestionList")){
            questionList.addAll(intent.getSerializableExtra("QuestionList") as ArrayList<QuestionModal>)
            setQuestion()
        }
    }

    private fun setQuestion() {
        binding.tvTotalScore.text = "Score: ${StartQuizActivity.totalScore.toString()}"
        binding.optionOne.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.optionTwo.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.optionThree.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.btnNextQuestion.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
        binding.optionOne.isEnabled = true
        binding.optionOne.isClickable = true
        binding.optionTwo.isEnabled = true
        binding.optionTwo.isClickable = true
        binding.optionThree.isEnabled = true
        binding.optionThree.isClickable = true
            question = questionList[questionIndex]
            val options:ArrayList<String> = arrayListOf()
            options.addAll(question?.options!!)
            var ansIndex = 0
            for (i in options.indices){
                if(options[i] == question!!.answer){
                    ansIndex = i
                }
            }

            options.removeAt(ansIndex)

            val threeOptions:ArrayList<String> = arrayListOf()
            threeOptions.addAll(options.take(2) as ArrayList<String>)
            threeOptions.add(question!!.answer)

            question?.options?.clear()
            question?.options?.addAll(threeOptions.shuffled())

            binding.tvQueCount.text = "Question ${questionIndex+1}"
            binding.tvQue.text = "${question?.question}"
            binding.optionOne.text = "${question?.options!![0]}"
            binding.optionTwo.text = "${question?.options!![1]}"
            binding.optionThree.text = "${question?.options!![2]}"

            startCountDownTimer()
    }

    private fun startCountDownTimer(){
        countDownTimer = object :CountDownTimer(20000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                if (countDownTimer != null) {
                    countDownTimer?.cancel()
                    countDownTimer = null
                    Log.e("NextQueIndex: ", "questionIndex: $questionIndex")
                    if (questionIndex < 9) {
                        Log.e("NextQueIndex: ", "questionIndex: if")
                        val rbSelectedOption =
                            findViewById<RadioButton>(binding.rgOptions.checkedRadioButtonId)
                        if (rbSelectedOption != null && rbSelectedOption.text == question?.answer) {
                            val remainSecond = binding.tvTimer.text.toString().toInt()
                            StartQuizActivity.totalCorrectAns =
                                StartQuizActivity.totalCorrectAns + 1
                            StartQuizActivity.totalScore =
                                StartQuizActivity.totalScore + 10 + remainSecond
                        }
                        questionIndex++
                        Log.e("NextQueIndex: ", "questionIndex after ++: $questionIndex")
                        binding.rgOptions.clearCheck()
                        setQuestion()
                    } else {
                        Log.e("NextQueIndex: ", "questionIndex: else")
                        val rbSelectedOption =
                            findViewById<RadioButton>(binding.rgOptions.checkedRadioButtonId)
                        if (rbSelectedOption != null && rbSelectedOption.text == question?.answer) {
                            val remainSecond = binding.tvTimer.text.toString().toInt()
                            StartQuizActivity.totalCorrectAns =
                                StartQuizActivity.totalCorrectAns + 1
                            StartQuizActivity.totalScore =
                                StartQuizActivity.totalScore + 10 + remainSecond
                        }
                        binding.rgOptions.clearCheck()

                        startActivity(Intent(this@QuizActivity, ResultActivity::class.java))
                        finish()
                    }
                }
            }
        }
        countDownTimer?.start()
    }
}