package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.ChangeMoneyEvent
import rocks.che.elections.logic.ChangeOpinionEvent
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.bus

class GameActivity : DefaultActivity() {
    private lateinit var view: GameView
    private lateinit var gamestate: Gamestate
    private val moneyToUp = 5

    fun buyGroupPoints(group: String): Boolean {
        if (gamestate.money < moneyToUp) {
            return false
        }
        gamestate.money -= moneyToUp
        bus.post(ChangeMoneyEvent(gamestate.money))
        gamestate.candidate.opinions[group]!!.inc() // FIXME check
        bus.post(ChangeOpinionEvent(group, gamestate.candidate.opinions[group]!!))
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gamestate = intent.extras.getParcelable("gamestate")

        view = GameView(gamestate)
        view.setContentView(this)
    }
}