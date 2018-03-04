package rocks.che.elections.minigames

import android.content.Intent
import android.os.Handler
import rocks.che.elections.DefaultActivity
import rocks.che.elections.NewGameActivity
import rocks.che.elections.logic.gamestate

abstract class MiniGameActivity(val nextActivity: Class<out DefaultActivity> = NewGameActivity::class.java) : DefaultActivity() {
    protected val handler = Handler()
    protected var score = 0
    protected val maxLose = 5
    open fun lose() {
        gamestate.money += score
        handler.removeCallbacksAndMessages(null)
        startActivity(Intent(this, nextActivity))
    }
}