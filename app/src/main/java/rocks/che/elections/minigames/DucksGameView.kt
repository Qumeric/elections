package rocks.che.elections.minigames

import androidx.core.content.ContextCompat
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView

class DucksGameView : AnkoComponent<DucksGameActivity> {
    lateinit var ankoContext: AnkoContext<DucksGameActivity>

    lateinit var layout: RelativeLayout
    lateinit var missedDucksText: TextView
    lateinit var missedShotsText: TextView
    lateinit var scoreText: TextView
    lateinit var crosshair: ImageView

    override fun createView(ui: AnkoContext<DucksGameActivity>) = with(ui) {
        ankoContext = ui

        val crosshairDrawable = ContextCompat.getDrawable(ctx, R.drawable.ic_crosshair)
        val bulletDrawable = ContextCompat.getDrawable(ctx, R.drawable.ic_bullet)

        linearLayout {
            gravity = Gravity.NO_GRAVITY

            orientation = LinearLayout.VERTICAL

            relativeLayout {
                backgroundResource = R.color.ducksBackground
                gravity = Gravity.CENTER
                missedDucksText = gameTextView(18, color = R.color.red) {
                }.lparams {
                    alignParentLeft()
                    centerVertically()
                }
                scoreText = gameTextView(18, color = R.color.white) {
                    textResource = R.string.ducks_welcome
                }.lparams {
                    centerInParent()
                }
                missedShotsText = gameTextView(18, color = R.color.red) {
                }.lparams {
                    alignParentRight()
                    centerVertically()
                }
            }.lparams(weight = 0.08f, width = matchParent, height = 0)

            linearLayout {
                backgroundResource = R.color.white
            }.lparams(weight = 0.12f, width = matchParent, height = 0)

            layout = relativeLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.drawable.ic_ducks_grass

                crosshair = imageView {
                    backgroundDrawable = crosshairDrawable
                    elevation = 100f
                }
            }.lparams(weight = 0.2f, width = matchParent, height = 0)

            linearLayout {
                gravity = Gravity.CENTER
                backgroundResource = R.color.ducksBackground

                imageButton {
                    backgroundDrawable = bulletDrawable

                    onClick {
                        ui.owner.shoot()
                    }
                }
            }.lparams(weight = 0.50f, width = matchParent, height = 0)
        }
    }
}
