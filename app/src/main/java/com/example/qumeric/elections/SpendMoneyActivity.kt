package com.example.qumeric.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView

class SpendMoneyActivity : DefaultActivity() {
    private lateinit var view: SpendMoneyView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = SpendMoneyView()
        view.setContentView(this)
    }
}