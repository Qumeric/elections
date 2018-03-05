package rocks.che.elections

import android.os.Bundle
import com.squareup.otto.Subscribe
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.BuyGroupPointsEvent
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.gamestate

class SpendMoneyActivity : DefaultActivity() {
    private lateinit var view: SpendMoneyView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = SpendMoneyView(gamestate.money, gamestate.questions.keys, bus)
        view.setContentView(this)
    }

    @Subscribe fun buyGroupPoints(e: BuyGroupPointsEvent) {
        gamestate.opinions[e.group]!! += e.amount
        gamestate.money -= e.totalSpent
    }
}