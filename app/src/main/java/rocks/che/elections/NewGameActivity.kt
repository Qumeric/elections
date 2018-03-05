package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.fakeCandidate
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.loadGame

class NewGameActivity: DefaultActivity() {
    private lateinit var view: NewGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FIXME saveGame last activity and bad code overall
        gamestate = loadGame()
        if (gamestate.candidate.name != fakeCandidate.name) {
            startActivity<GameActivity>()
        }

        view = NewGameView()
        view.setContentView(this)
    }
}