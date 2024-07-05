package com.example.devendraquizapppractical.modal

import java.io.Serializable

class QuestionModal: Serializable {
    var id: Int = 0
    var question: String = ""
    var options: ArrayList<String> = arrayListOf()
    var answer: String = ""
}