package rocks.che.elections.minigames

import android.graphics.Rect
import android.os.Bundle
import android.widget.ImageView
import org.jetbrains.anko.ctx
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.setContentView
import rocks.che.elections.R
import java.lang.Math.random

class DucksGameActivity : MiniGameActivity() {
    private lateinit var view: DucksGameView

    private var missedShots = 0
    private var missedDucks = 0

    val ducks: MutableSet<ImageView> = mutableSetOf()

    private val createDuck = object : Runnable {
        override fun run() {
            val duckView = ImageView(ctx)

            duckView.x = displayMetrics.widthPixels.toFloat()

            duckView.setImageResource(R.drawable.ic_duck)

            view.layout.addView(duckView)
            ducks.add(duckView)

            handler.postDelayed(this, ((10000 * (1 + random())) / Math.sqrt(10.0 + score)).toLong())
        }
    }

    private val update = object : Runnable {
        override fun run() {
            val toRemove: MutableList<ImageView> = mutableListOf()

            for (s in ducks) {
                s.x -= Math.sqrt(10.0 + score).toFloat()
                s.invalidate()

                if (s.x <= -s.drawable.intrinsicWidth) {
                    toRemove.add(s)
                    missedDucks++
                    lose()
                    return
                }
            }

            for (s in toRemove) {
                ducks.remove(s)
                view.layout.removeView(s)
            }

            view.scoreText.text = "Score: %d, missed ducks: %d, missed shots: %d".format(
                    score, missedDucks, missedShots)

            handler.postDelayed(this, (1000 / 50).toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = DucksGameView()
        view.setContentView(this)

        drawInformationDialog(getString(R.string.ducks_info_title), getString(R.string.ducks_info_message),
                {
                    handler.postDelayed(createDuck, 1)
                    handler.postDelayed(update, 1)
                }, view.ankoContext)
    }

    fun shoot() {
        val ch_rc = Rect()
        view.crosshair.getHitRect(ch_rc)

        val coef = 0.1

        ch_rc.bottom = ch_rc.centerY() + (ch_rc.bottom - ch_rc.centerY()) * coef.toInt()
        ch_rc.top = ch_rc.centerY() + (ch_rc.top - ch_rc.centerY()) * coef.toInt()
        ch_rc.left = ch_rc.centerX() + (ch_rc.left - ch_rc.centerX()) * coef.toInt()
        ch_rc.right = ch_rc.centerX() + (ch_rc.right - ch_rc.centerX()) * coef.toInt()

        var isHit = false
        for (duck in ducks) {
            val duck_rc = Rect()
            duck.getHitRect(duck_rc)

            // Constants hardcoded for the specific asset
            val realDuckCenterX = duck_rc.left + (duck_rc.right - duck_rc.left) * 312 / 512
            val realDuckCenterY = duck_rc.top + (duck_rc.bottom - duck_rc.top) * 287 / 512

            if (ch_rc.contains(realDuckCenterX, realDuckCenterY)) {
                ducks.remove(duck)
                view.layout.removeView(duck)
                score++
                isHit = true
                break
            }
        }
        if (!isHit) {
            missedShots++
        }
    }
}
