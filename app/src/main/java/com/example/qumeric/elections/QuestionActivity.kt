package com.example.qumeric.elections

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

class QuestionActivity : DefaultActivity() {
    private lateinit var view: QuestionView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val question: Question = intent.extras.getSerializable("question") as Question
        val group: String = intent.extras.getString("group")

        view = QuestionView(question, group)
        view.setContentView(this)
    }
}
