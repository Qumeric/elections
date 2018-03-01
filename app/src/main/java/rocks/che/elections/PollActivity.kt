package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.logic.gamestate

class PollActivity : DefaultActivity() {
    private lateinit var view: PollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val h = mutableMapOf<String, List<Double>>(gamestate.candidate.name to gamestate.candidate.history)
        for (c in gamestate.candidates) {
            h[c.name] = c.history
        }

        view = PollView(h)
        view.setContentView(this)


        if (!gamestate.isLost()) {
            view.expelledTV.text = String.format("Candidate %s has been expelled from elections", gamestate.expel())
        }
    }
}

