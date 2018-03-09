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
    private lateinit var gs: Gamestate
    private val moneyToUp = 5

    fun buyGroupPoints(group: String): Boolean {
        if (gs.money < moneyToUp) {
            return false
        }
        gs.money -= moneyToUp
        bus.post(ChangeMoneyEvent(gs.money))
        gs.candidate.opinions[group] = gs.candidate.opinions[group]!!+1
        bus.post(ChangeOpinionEvent(group, gs.candidate.opinions[group]!!))
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            gs = intent.extras.getParcelable("gamestate")
        } catch (e: Exception) {
            gs = Gamestate.loadGame()!!
        }
        gs.save()

        view = GameView(gs)
        view.setContentView(this)
    }
}