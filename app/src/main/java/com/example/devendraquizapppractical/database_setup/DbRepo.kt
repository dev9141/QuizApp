package com.example.devendraquizapppractical.database_setup

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.devendraquizapppractical.modal.QuestionModal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DbRepo {
    private const val TABLE_NAME = "QuizTable"
    private const val COL_QUESTION_ID = "QID"
    private const val COL_QUESTION = "Question"
    private const val COL_OPTIONS = "Options"
    private const val COL_ANSWER = "Answer"

    const val CREATE_TABLE_QUERY = """CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                 $COL_QUESTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                 $COL_QUESTION TEXT,
                 $COL_OPTIONS TEXT,
                 $COL_ANSWER TEXT
             )"""


    fun insertQuestions(db: QuizDatabaseHelper, questionList: ArrayList<QuestionModal>): Boolean {
        val db = db.writableDatabase
        db!!.beginTransaction()
        var isQuestionInserted = false
        isQuestionInserted = try {
            val values = ContentValues()
            for (question in questionList) {
                values.put(COL_QUESTION, question.question)
                val gson = Gson()
                val optionJson = gson.toJson(question.options)
                values.put(COL_OPTIONS, optionJson)
                values.put(COL_ANSWER, question.answer)
                db.insert(TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            if (db != null && db.isOpen) {
                db.endTransaction()
                //db.close();
            }
        }
        return isQuestionInserted
    }

    @SuppressLint("Range")
    fun getAllQuestion(db: QuizDatabaseHelper): ArrayList<QuestionModal> {
        val allQuestionList: ArrayList<QuestionModal> = arrayListOf()

        val getAllQuestionQuery = "SELECT * FROM $TABLE_NAME"

        val db = db.readableDatabase
        val cursor = db!!.rawQuery(getAllQuestionQuery, null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            do {
                // prepare note object
                allQuestionList.add(
                    QuestionModal().apply {
                        id = cursor.getInt(cursor.getColumnIndex(COL_QUESTION_ID))
                        question = cursor.getString(cursor.getColumnIndex(COL_QUESTION))
                        val gson = Gson()
                        val itemType = object : TypeToken<ArrayList<String>>() {}.type
                        val option: ArrayList<String> = gson.fromJson(cursor.getString(cursor.getColumnIndex(COL_OPTIONS)), itemType)
                        options = option
                        answer = cursor.getString(cursor.getColumnIndex(COL_ANSWER))
                    }
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        //db.let{ it.close() }

        return allQuestionList
    }

    @SuppressLint("Range")
    fun getRandomTenQuestions(db: QuizDatabaseHelper): List<QuestionModal> {
        val questionList = mutableListOf<QuestionModal>()
        val db = db.readableDatabase
        val getRandomTenQuestionsQuery = "SELECT * FROM $TABLE_NAME ORDER BY RANDOM() LIMIT 10"
        val cursor = db.rawQuery(getRandomTenQuestionsQuery, null)

        if (cursor.count > 0) {
            cursor.moveToFirst()
            do {
                // prepare note object
                questionList.add(
                    QuestionModal().apply {
                        id = cursor.getInt(cursor.getColumnIndex(COL_QUESTION_ID))
                        question = cursor.getString(cursor.getColumnIndex(COL_QUESTION))
                        val gson = Gson()
                        val itemType = object : TypeToken<ArrayList<String>>() {}.type
                        val option: ArrayList<String> = gson.fromJson(cursor.getString(cursor.getColumnIndex(COL_OPTIONS)), itemType)
                        options.addAll(option)
                        answer = cursor.getString(cursor.getColumnIndex(COL_ANSWER))
                    }
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        //db.let{ it.close() }
        return questionList
    }
}