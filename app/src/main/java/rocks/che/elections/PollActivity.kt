package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate

class PollActivity : DefaultActivity() {
    private lateinit var view: PollView
    private lateinit var gs: Gamestate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gs = intent.getParcelableExtra("gamestate")
        gs.updateCandidate()

        val h = gs.candidates.map { it.resource to it.history}.toMap()

        view = PollView(h, gs)

        view.setContentView(this)
    }
}
