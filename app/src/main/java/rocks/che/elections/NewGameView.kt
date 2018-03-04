package rocks.che.elections

import android.view.Gravity
import android.widget.LinearLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.debate.DebateActivity
import rocks.che.elections.minigames.*

class NewGameView : AnkoComponent<NewGameActivity> {
    private lateinit var ankoContext: AnkoContext<NewGameActivity>

    override fun createView(ui: AnkoContext<NewGameActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            gravity = Gravity.CENTER

            imageButton {
                imageResource = R.drawable.play
                onClick {
                    ctx.startActivity(ctx.intentFor<ChooseCandidateActivity>())
                }
            }
            themedButton(theme = R.style.button) {
                text = "c"
                onClick {
                    ctx.startActivity(ctx.intentFor<CatcherGameActivity>())
                }
            }
            themedButton(theme = R.style.button) {
                text = "ducks"
                onClick {
                    ctx.startActivity(ctx.intentFor<DucksGameActivity>())
                }
            }
            themedButton(theme = R.style.button) {
                text = "hammer"
                onClick {
                    ctx.startActivity(ctx.intentFor<HammerGameActivity>())
                }
            }
            themedButton(theme = R.style.button) {
                text = "ladder"
                onClick {
                    ctx.startActivity(ctx.intentFor<LadderGameActivity>())
                }
            }
            themedButton(theme = R.style.button) {
                text = "snake"
                onClick {
                    ctx.startActivity(ctx.intentFor<SnakeGameActivity>())
                }
            }
            themedButton(theme = R.style.button) {
                text = "runner"
                onClick {
                    ctx.startActivity(ctx.intentFor<RunnerGameActivity>())
                }
            }
            themedButton(theme = R.style.button) {
                text = "debate"
                onClick {
                    ctx.startActivity(ctx.intentFor<DebateActivity>())
                }
            }
        }
    }
}
