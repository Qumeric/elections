package com.example.qumeric.elections

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

class QuestionActivity : AppCompatActivity() {
    private lateinit var view: QuestionView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val question : Question = intent.extras.getSerializable("question") as Question

        view = QuestionView(question)
        view.setContentView(this)
    }
}
