package rocks.che.elections.minigames

import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.verticalLayout

class RunnerGameView : AnkoComponent<RunnerGameActivity> {
    lateinit var ankoContext: AnkoContext<RunnerGameActivity>

    override fun createView(ui: AnkoContext<RunnerGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout()
    }
}
