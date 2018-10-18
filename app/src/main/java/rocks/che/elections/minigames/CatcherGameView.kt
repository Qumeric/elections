package rocks.che.elections.minigames

import android.view.Gravity
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onTouch
import rocks.che.elections.R
import rocks.che.elections.helpers.gameTextView

class CatcherGameView : AnkoComponent<CatcherGameActivity> {
    lateinit var ankoContext: AnkoContext<CatcherGameActivity>

    lateinit var layout: RelativeLayout
    lateinit var scoreText: TextView
    lateinit var missedText: TextView
    lateinit var cart: ImageButton

    override fun createView(ui: AnkoContext<CatcherGameActivity>) = with(ui) {
        ankoContext = ui

        val cartDrawable = ui.ctx.resources.getDrawable(R.drawable.ic_cart, ctx.theme)

        layout = relativeLayout {
            gravity = Gravity.NO_GRAVITY
            backgroundResource = R.color.aqua

            relativeLayout {
                backgroundResource = R.color.black
                background.alpha = 33
                gravity = Gravity.CENTER
                scoreText = gameTextView(18) {
                }.lparams {
                    alignParentLeft()
                }
                missedText = gameTextView(18, R.color.maroon) {
                }.lparams {
                    alignParentRight()
                }
            }.lparams(height = dip(50), width = matchParent)

            onTouch { _, e ->
                val cartWidth = cartDrawable.intrinsicWidth.toFloat()
                cart.x = Math.max(0f, Math.min(e.x - cartWidth / 2, displayMetrics.widthPixels - cartWidth))
                true

            }

            cart = imageButton {
                backgroundDrawable = cartDrawable
                y = (displayMetrics.heightPixels - cartDrawable.intrinsicHeight).toFloat()

                isClickable = false
            }


        }
        layout
    }
}
