package rocks.che.elections.minigames

import android.graphics.Rect
import android.os.Bundle
import android.widget.ImageView
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.ctx
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.image
import org.jetbrains.anko.setContentView
import rocks.che.elections.R
import java.lang.Math.random

class CatcherGameActivity : MiniGameActivity() {
    private lateinit var view: CatcherGameView

    var missedStrawberries = 0

    val strawberries: MutableSet<ImageView> = mutableSetOf()

    private val createStrawberry = object : Runnable {
        override fun run() {
            val strawberryView = ImageView(ctx)

            strawberryView.setImageResource(R.drawable.ic_strawberry)
            val sWidth = strawberryView.image!!.intrinsicWidth
            strawberryView.x = (displayMetrics.widthPixels - sWidth) * random().toFloat()

            view.layout.addView(strawberryView)
            strawberries.add(strawberryView)

            handler.postDelayed(this, (1000 / Math.sqrt(1.0 + score)).toLong())
        }
    }

    val update = object : Runnable {
        override fun run() {
            val cart_rc = Rect()
            view.cart.getHitRect(cart_rc)

            val toRemove: MutableList<ImageView> = mutableListOf()

            for (s in strawberries) {
                s.y += 10f + score
                s.invalidate()

                val s_rc = Rect()
                s.getHitRect(s_rc)

                if (cart_rc.intersect(s_rc)) {
                    MusicManager.getInstance().play(ctx, R.raw.catch_strawberry_sound)
                    score++
                    toRemove.add(s)
                }

                if (s.y + s.height >= view.layout.height) {
                    MusicManager.getInstance().play(ctx, R.raw.catch_miss_sound)
                    missedStrawberries++
                    toRemove.add(s)
                }
            }

            for (s in toRemove) {
                strawberries.remove(s)
                view.layout.removeView(s)
            }

            view.scoreText.text = score.toString()
            view.missedText.text = missedStrawberries.toString()

            if (missedStrawberries >= maxLose) {
                lose()
                return
            }

            handler.postDelayed(this, (1000 / 50).toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = CatcherGameView()
        view.setContentView(this)

        drawInformationDialog(getString(R.string.ducks_info_title), getString(R.string.ducks_info_message),
                {
                    handler.postDelayed(createStrawberry, 1)
                    handler.postDelayed(update, 1)
                }, view.ankoContext)
    }

    override fun lose() {
        handler.removeCallbacksAndMessages(null)
        drawInformationDialog(
                getString(R.string.catcher_end_title),
                getString(R.string.catcher_end_message_template).format(score),
                { super.lose() },
                view.ankoContext
        )
    }
}
