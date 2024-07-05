package com.example.devendraquizapppractical.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.devendraquizapppractical.R
import com.example.devendraquizapppractical.database_setup.DbRepo
import com.example.devendraquizapppractical.database_setup.QuizDatabaseHelper
import com.example.devendraquizapppractical.databinding.ActivityStartQuizBinding
import com.example.devendraquizapppractical.modal.QuestionModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartQuizActivity : AppCompatActivity() {

    companion object{
        var totalScore: Int = 0
        var totalCorrectAns: Int = 0
    }

    private lateinit var binding: ActivityStartQuizBinding
    private lateinit var dbInstance: QuizDatabaseHelper
    private val questionList: ArrayList<QuestionModal> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_quiz)

        // Enable JavaScript (optional)
        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        // Handle navigation in the WebView
        binding.webView.webViewClient = WebViewClient()

        // Handle JavaScript alerts, dialogs, etc.
        binding.webView.webChromeClient = WebChromeClient()

        // Load a webpage
        val url = "http://ec2-3-93-179-113.compute-1.amazonaws.com/"
        binding.webView.loadUrl(url)


        dbInstance = QuizDatabaseHelper.getInstance(this)
        CoroutineScope(Dispatchers.IO).launch {
            insertQuestion()
        }
        binding.btnPlayQuiz.setOnClickListener {
            questionList.clear()
            questionList.addAll(DbRepo.getRandomTenQuestions(dbInstance))
            startActivity(Intent(this, QuizActivity::class.java).apply {
                putExtra("QuestionList", questionList)
                putExtra("QuestionIndex", 0)
            })
        }

    }

    private fun insertQuestion() {
        if (DbRepo.getAllQuestion(dbInstance).isNotEmpty() ) {
            return
        }

        val questionList: ArrayList<QuestionModal> = arrayListOf()
        questionList.addAll(
            listOf(
                    QuestionModal().apply {
                    question = "Who is the Prime Minister of India?"
                    options.addAll(listOf("Narendra Modi", "Rahul Gandhi", "Manmohan Singh", "Amit Shah"))
                    answer = "Narendra Modi"
                },
                QuestionModal().apply {
                    question = "What is the capital of India?"
                    options.addAll(listOf("Mumbai", "Chennai", "Delhi", "Ahmedabad"))
                    answer = "Delhi"
                },
                QuestionModal().apply {
                    question = " What is sum of 15 + 25 ?"
                    options.addAll(listOf("5", "25", "40", "None"))
                    answer = "40"
                },
                QuestionModal().apply {
                    question = "Which one is maximum? 25, 11, 17, 18, 40, 42"
                    options.addAll(listOf("11", "42", "17", "None"))
                    answer = "42"
                },
                QuestionModal().apply {
                    question = "What is the official language of Gujarat? "
                    options.addAll(listOf("Hindi", "Gujarati", "Marathi", "None"))
                    answer = "Gujarati"
                },
                QuestionModal().apply {
                    question = "What is multiplication of 12 * 12 ?"
                    options.addAll(listOf("124", "12", "24", "None"))
                    answer = "None"
                },
                QuestionModal().apply {
                    question = "Which state of India has the largest population?"
                    options.addAll(listOf("UP", "Bihar", "Gujarat", "Maharashtra"))
                    answer = "UP"
                },
                QuestionModal().apply {
                    question = "Who is the Home Minister of India?"
                    options.addAll(listOf("Amit Shah", "Rajnath Singh", "Narendra Modi", "None"))
                    answer = "Amit Shah"
                },
                QuestionModal().apply {
                    question = "What is the capital of Gujarat?"
                    options.addAll(listOf("Vadodara", "Ahmedabad", "Gandhinagar", "Rajkot"))
                    answer = "Gandhinagar"
                },
                QuestionModal().apply {
                    question = "Which number will be next in series? 1, 4, 9, 16, 25"
                    options.addAll(listOf("21", "36", "49", "32"))
                    answer = "36"
                },
                QuestionModal().apply {
                    question = "Which one is minimum? 5, 0, -20, 11"
                    options.addAll(listOf("0", "11", "-20", "None"))
                    answer = "-20"
                },
                QuestionModal().apply {
                    question = "What is sum of 10, 12 and 15?"
                    options.addAll(listOf("37", "25", "10", "12"))
                    answer = "37"
                },
                QuestionModal().apply {
                    question = "What is the official language of the Government of India?"
                    options.addAll(listOf("Hindi", "English", "Gujarati", "None"))
                    answer = "Hindi"
                },
                QuestionModal().apply {
                    question = "Which country is located in Asia?"
                    options.addAll(listOf("India", "USA", "UK", "None"))
                    answer = "India"
                },
                QuestionModal().apply {
                    question = "Which language(s) is/are used for Android app development?"
                    options.addAll(listOf("Java", "Java & Kotlin", "Kotlin", "Swift"))
                    answer = "Java & Kotlin"
                }
            )
        )
        DbRepo.insertQuestions(dbInstance, questionList)
    }
}