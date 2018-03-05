package rocks.che.elections

import com.squareup.otto.Bus
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.BuyGroupPointsEvent
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.fakeQuestions
import rocks.che.elections.logic.getGroupResource

class SpendMoneyView(var money: Int = 0, val questions: Iterable<String> = fakeQuestions.keys,
                     val bus: Bus = Bus()) : DefaultView<SpendMoneyActivity> {
    private lateinit var ankoContext: AnkoContext<SpendMoneyActivity>

    private val moneyToUp = 5

    override fun createView(ui: AnkoContext<SpendMoneyActivity>) = with(ui) {
        ankoContext = ui

        verticalLayout {
            val moneyTextView = gameTextView {
                text = money.toString()
            }

            for (group in questions) {
                imageView {
                    onClick {
                        if (money >= moneyToUp) {
                            bus.post(BuyGroupPointsEvent(group, 1, moneyToUp))
                            money -= moneyToUp
                            moneyTextView.text = money.toString()
                        } else {
                            snackbar(this, R.string.not_enough_money)
                        }
                    }
                    imageResource = getGroupResource(group)
                }
            }

            themedButton(theme = R.style.button) {
                textResource = R.string.finish
                onClick {
                    ctx.startActivity<GameActivity>()
                }
            }
        }
    }
}
