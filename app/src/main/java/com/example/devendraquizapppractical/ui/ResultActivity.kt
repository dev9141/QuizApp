package com.example.devendraquizapppractical.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.devendraquizapppractical.R
import com.example.devendraquizapppractical.database_setup.DbRepo
import com.example.devendraquizapppractical.database_setup.QuizDatabaseHelper
import com.example.devendraquizapppractical.databinding.ActivityResultBinding
import com.example.devendraquizapppractical.modal.QuestionModal

class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding
    private val questionList: ArrayList<QuestionModal> = arrayListOf()
    private lateinit var dbInstance: QuizDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_result)
        dbInstance = QuizDatabaseHelper.getInstance(this)
        binding.tvTotalScore.text = "Total Score: ${StartQuizActivity.totalScore}"
        binding.btnPlayAgain.setOnClickListener {
            questionList.clear()
            questionList.addAll(DbRepo.getRandomTenQuestions(dbInstance))
            startActivity(Intent(this, QuizActivity::class.java).apply {
                StartQuizActivity.totalCorrectAns = 0
                StartQuizActivity.totalScore = 0
                putExtra("QuestionList", questionList)
                putExtra("QuestionIndex", 0)
            })
            finish()
        }

        when (StartQuizActivity.totalCorrectAns) {
            in 0 .. 2 -> {
                binding.tvResultMessage.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.tvResultMessage.text = "Sorry, You failed."
            }
            in 3..4 -> {
                binding.tvResultMessage.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.tvResultMessage.text = "Well played but you failed. All The Best for Next Game."
            }
            5 -> {
                binding.tvResultMessage.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvResultMessage.text = "You Won!"
            }
            in 6..7 -> {
                binding.tvResultMessage.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvResultMessage.text = "You Won! Congratulations."
            }
            in 8 .. 9 -> {
                binding.tvResultMessage.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvResultMessage.text = "Congratulations and Well Done."
            }
            10 -> {
                binding.tvResultMessage.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.tvResultMessage.text = "You are Genius. Congratulations you won the Game."
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        StartQuizActivity.totalCorrectAns = 0
        StartQuizActivity.totalScore = 0
        startActivity(Intent(this, StartQuizActivity::class.java))
        finish()
    }
}