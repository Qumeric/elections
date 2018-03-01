package rocks.che.elections

import android.view.Gravity
import android.widget.LinearLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
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
            button {
                text = "d"
                onClick {
                    ctx.startActivity(ctx.intentFor<DucksGameActivity>())
                }
            }
            button {
                text = "h"
                onClick {
                    ctx.startActivity(ctx.intentFor<HammerGameActivity>())
                }
            }
            button {
                text = "l"
                onClick {
                    ctx.startActivity(ctx.intentFor<LadderGameActivity>())
                }
            }
            button {
                text = "s"
                onClick {
                    ctx.startActivity(ctx.intentFor<SnakeGameActivity>())
                }
            }
        }
    }
}
