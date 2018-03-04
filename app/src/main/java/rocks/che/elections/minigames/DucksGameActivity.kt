package rocks.che.elections.minigames

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import im.delight.android.audio.MusicManager
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

    private fun createDuck() {
        val duckView = ImageView(ctx)

        duckView.x = displayMetrics.widthPixels.toFloat()

        duckView.setImageResource(R.drawable.ic_duck)

        view.layout.addView(duckView)
        ducks.add(duckView)

        handler.postDelayed({createDuck()}, ((5000 * (0.7 + random())) / Math.sqrt(10.0 + score)).toLong())
    }

    private fun update() {
        val toRemove: MutableList<ImageView> = mutableListOf()

        for (s in ducks) {
            s.x -= 3f + Math.sqrt(5f+score.toDouble()).toFloat()

            if (s.x <= -s.drawable.intrinsicWidth) {
                toRemove.add(s)
                missedDucks++
            }
        }

        for (s in toRemove) {
            ducks.remove(s)
            view.layout.removeView(s)
        }

        view.scoreText.text = score.toString()
        view.missedDucksText.text = missedDucks.toString()
        view.missedShotsText.text = missedShots.toString()

        if (missedDucks + missedShots >= maxLose) {
            lose()
            return
        }

        handler.postDelayed({update()}, (1000 / 50).toLong())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = DucksGameView()
        view.setContentView(this)

        drawInformationDialog(getString(R.string.ducks_info_title), getString(R.string.ducks_info_message),
                {
                    handler.postDelayed({createDuck()}, 1)
                    handler.postDelayed({update()}, 1)
                }, view.ankoContext)
    }

    fun shoot() {
        MusicManager.getInstance().play(this, R.raw.shot_sound);

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

            // Constants are hardcoded for the specific duck drawable
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

    override fun lose() {
        handler.removeCallbacksAndMessages(null)
        drawInformationDialog(
                getString(R.string.ducks_end_title),
                getString(R.string.ducks_end_message_template).format(score),
                { super.lose() },
                view.ankoContext
        )
    }
}
