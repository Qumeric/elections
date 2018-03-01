package rocks.che.elections.debate

import org.jetbrains.anko.*

class DebateViewStart : AnkoComponent<DebateActivity> {
    private lateinit var ankoContext: AnkoContext<DebateActivity>

    override fun createView(ui: AnkoContext<DebateActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
        }
    }
}
