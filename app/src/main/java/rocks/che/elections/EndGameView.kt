package rocks.che.elections

import android.graphics.Color
import android.view.Gravity
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.jetbrains.anko.*
import rocks.che.elections.helpers.konfettiView

class EndGameView : AnkoComponent<EndGameActivity> {
    private lateinit var ankoContext: AnkoContext<EndGameActivity>

    override fun createView(ui: AnkoContext<EndGameActivity>) = with(ui) {
        ankoContext = ui

        frameLayout {
            konfettiView {
                build().addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.RECT, Shape.CIRCLE)
                        .addSizes(Size(12))
                        .setPosition(-50f, this.width + 50f, -50f, -50f)
                        .streamFor(300, 5000L)
            }
            verticalLayout {
                gravity = Gravity.CENTER
                themedButton(theme = R.style.button) {
                    textResource = R.string.congratulations
                }
            }
        }
    }
}
