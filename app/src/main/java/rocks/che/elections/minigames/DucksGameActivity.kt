package rocks.che.elections.minigames

import android.graphics.Rect
import android.os.Bundle
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

    private val ducks: MutableSet<ImageView> = mutableSetOf()

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

        MusicManager.instance.play(this, R.raw.ducks_music)

        drawInformationDialog(getString(R.string.ducks_info_title), getString(R.string.ducks_info_message),
                {
                    handler.postDelayed({createDuck()}, 1)
                    handler.postDelayed({update()}, 1)
                }, view.ankoContext)
    }

    fun shoot() {
        playSound(R.raw.shot_sound)
        val chRC = Rect()
        view.crosshair.getHitRect(chRC)

        val coef = 0.1

        chRC.bottom = chRC.centerY() + (chRC.bottom - chRC.centerY()) * coef.toInt()
        chRC.top = chRC.centerY() + (chRC.top - chRC.centerY()) * coef.toInt()
        chRC.left = chRC.centerX() + (chRC.left - chRC.centerX()) * coef.toInt()
        chRC.right = chRC.centerX() + (chRC.right - chRC.centerX()) * coef.toInt()

        var isHit = false
        for (duck in ducks) {
            val duckRC = Rect()
            duck.getHitRect(duckRC)

            // Constants are hardcoded for the specific duck drawable
            val realDuckCenterX = duckRC.left + (duckRC.right - duckRC.left) * 312 / 512
            val realDuckCenterY = duckRC.top + (duckRC.bottom - duckRC.top) * 287 / 512

            if (chRC.contains(realDuckCenterX, realDuckCenterY)) {
                playSound(R.raw.duck_hit_sound)
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
