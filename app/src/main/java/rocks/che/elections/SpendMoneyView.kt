package rocks.che.elections

import android.support.v4.content.res.ResourcesCompat
import android.widget.Toast
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.logic.gamestate

class SpendMoneyView : AnkoComponent<SpendMoneyActivity> {
    private lateinit var ankoContext: AnkoContext<SpendMoneyActivity>

    private val moneyToUp = 5

    override fun createView(ui: AnkoContext<SpendMoneyActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            val moneyTextView = textView {
                text = gamestate.money.toString()
                typeface = ResourcesCompat.getFont(ctx, R.font.mfred)
            }

            for ((group, qGroup) in gamestate.questions) {
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
                    imageResource = resources.getIdentifier(group, "drawable", "com.example.qumeric.elections")
                }
            }

            button {
                textResource = R.string.finish
                onClick {
                    ctx.startActivity(ctx.intentFor<GameActivity>())
                }
            }
        }
    }
}
