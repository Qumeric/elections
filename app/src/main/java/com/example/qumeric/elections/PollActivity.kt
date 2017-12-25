package com.example.qumeric.elections

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

class PollActivity : AppCompatActivity() {
    private lateinit var view: PollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = PollView()
        view.setContentView(this)
    }
}