package com.example.qumeric.elections

import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.verticalLayout

class RunnerGameView : AnkoComponent<RunnerGameActivity> {
    private lateinit var ankoContext: AnkoContext<RunnerGameActivity>

    override fun createView(ui: AnkoContext<RunnerGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout()
    }
}
