package rocks.che.elections

import android.widget.Toast
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.getGroupResource

class SpendMoneyView : AnkoComponent<SpendMoneyActivity> {
    private lateinit var ankoContext: AnkoContext<SpendMoneyActivity>

    private val moneyToUp = 5

    override fun createView(ui: AnkoContext<SpendMoneyActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            val moneyTextView = gameTextView {
                text = gamestate.money.toString()
            }

            for (group in gamestate.questions.keys) {
                imageView {
                    onClick {
                        if (gamestate.money >= moneyToUp) {
                            gamestate.opinions[group]!!.add(1)
                            gamestate.money -= 5
                            moneyTextView.text = gamestate.money.toString()
                        } else {
                            Toast.makeText(ctx, ctx.getString(R.string.not_enough_money), Toast.LENGTH_SHORT).show()
                        }
                    }
                    imageResource = getGroupResource(group)
                }
            }

            themedButton(theme = R.style.button) {
                textResource = R.string.finish
                onClick {
                    ctx.startActivity(ctx.intentFor<GameActivity>())
                }
            }
        }
    }
}
