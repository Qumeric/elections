package com.example.qumeric.elections

import android.content.Intent
import android.os.Handler

abstract class MiniGameActivity(val nextActivity: Class<out DefaultActivity> = NewGameActivity::class.java) : DefaultActivity() {
    protected val handler = Handler()
    protected var score = 0
    fun lose() {
        gamestate.money += score
        handler.removeCallbacksAndMessages(null)
        startActivity(Intent(this, nextActivity))
    }
}