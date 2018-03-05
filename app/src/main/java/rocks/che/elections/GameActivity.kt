package rocks.che.elections

import android.os.Bundle
import com.squareup.otto.Subscribe
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.BuyGroupPointsEvent
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.gamestate

class GameActivity : DefaultActivity() {
    private lateinit var view: GameView

    @Subscribe fun buyGroupPoints(e: BuyGroupPointsEvent) {
        gamestate.opinions[e.group]!! += e.amount
        gamestate.money -= e.totalSpent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = GameView(gamestate.step, gamestate.questions, gamestate.opinions, gamestate.money, bus=bus)
        view.setContentView(this)
    }
}