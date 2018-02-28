package com.example.qumeric.elections

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.setContentView

class MainActivity : DefaultActivity() {
    private lateinit var view: MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = MainView()
        view.setContentView(this)
    }
}
