package rocks.che.elections

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import com.pixplicity.easyprefs.library.Prefs
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.helpers.konfettiView

class EndGameView(val isMutin: Boolean = false) : DefaultView<EndGameActivity> {
    private lateinit var ankoContext: AnkoContext<EndGameActivity>

    @SuppressLint("RtlHardcoded")
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
                weightSum = 1f
                horizontalPadding = dip(30)
                verticalLayout {
                    gravity = Gravity.CENTER
                    backgroundResource = R.color.white
                    gameTextView(20) {
                        textResource = R.string.congratulations
                    }.lparams {
                        width = matchParent
                    }
                    gameTextView(12) {
                        textResource = if (isMutin) {
                            R.string.endgame_text
                        } else {
                            R.string.endgame_text_mutin
                        }
                    }.lparams {
                        width = matchParent
                    }
                    themedButton(theme = R.style.button) {
                        onClick {
                            Prefs.remove("gamestate")
                            ctx.startActivity<NewGameActivity>()
                        }
                        textResource = R.string.try_again
                    }.lparams {
                        width = matchParent
                        gravity = Gravity.BOTTOM
                    }
                }.lparams {
                    height = 0
                    weight = 0.5f
                }
            }.lparams(width= matchParent, height = matchParent) {
            }
            val imgSize = dip(150)
            imageView {
                rotation = -30f
                imageResource = R.drawable.candidate_putin
                x = displayMetrics.widthPixels/2.toFloat()
                y = imgSize/2.toFloat()
            }.lparams(height = imgSize) {
                gravity = Gravity.BOTTOM
            }
        }
    }
}
