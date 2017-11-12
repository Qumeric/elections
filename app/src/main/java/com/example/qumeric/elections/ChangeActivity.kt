package com.example.qumeric.elections

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView

class ChangeActivity : AppCompatActivity() {
    private lateinit var view: ChangeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val answer : Answer = intent.extras.getSerializable("answer") as Answer

        val oldValues = gamestate.opinions.mapValues{it.value.value}
        answer.select()

        view = ChangeView(oldValues)
        view.setContentView(this)
    }
}