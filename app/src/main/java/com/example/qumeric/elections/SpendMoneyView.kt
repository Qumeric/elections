package com.example.qumeric.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick

class SpendMoneyView : AnkoComponent<SpendMoneyActivity> {
    private lateinit var ankoContext: AnkoContext<SpendMoneyActivity>

    override fun createView(ui: AnkoContext<SpendMoneyActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout { }
    }
}
