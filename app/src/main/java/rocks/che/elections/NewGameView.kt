package rocks.che.elections

import android.view.Gravity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.debate.DebateActivity
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.minigames.*

class NewGameView : DefaultView<NewGameActivity> {
    private lateinit var ankoContext: AnkoContext<NewGameActivity>

    override fun createView(ui: AnkoContext<NewGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            imageButton {
                imageResource = R.drawable.play
                onClick {
                    ctx.startActivity<ChooseCandidateActivity>()
                }
            }
            button {
                text = "c"
                onClick {
                    ctx.startActivity<CatcherGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "ducks"
                onClick {
                    ctx.startActivity<DucksGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "hammer"
                onClick {
                    ctx.startActivity<HammerGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "ladder"
                onClick {
                    ctx.startActivity<LadderGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "snake"
                onClick {
                    ctx.startActivity<SnakeGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "runner"
                onClick {
                    ctx.startActivity<RunnerGameActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "debate"
                onClick {
                    ctx.startActivity<DebateActivity>()
                }
            }
            themedButton(theme = R.style.button) {
                text = "endGameView"
                onClick {
                    ctx.startActivity<EndGameActivity>()
                }
            }
        }
    }
}
