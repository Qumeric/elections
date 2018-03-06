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

class RunnerGameActivity : MiniGameActivity() {
    private lateinit var view: RunnerGameView
    private val jumpVelocity = 100
    private val gravity = 12
    private var currentVelocity = 0

    private val cars: MutableSet<ImageView> = mutableSetOf()

    private fun createCar() {
        val carView = ImageView(ctx)

        carView.setImageResource(R.drawable.ic_police_car)

        view.layout.addView(carView)
        cars.add(carView)

        carView.x = displayMetrics.widthPixels.toFloat()
        carView.y = view.stickmanView.height.toFloat()-carView.drawable.intrinsicHeight
        Log.d("CreateCar", "y is " + carView.y)

        handler.postDelayed({createCar()}, ((5000 * (0.7 + random())) / Math.sqrt(10.0 + score)).toLong())
    }

    private fun isLost(): Boolean {
        val smRC = Rect()
        view.stickmanView.getHitRect(smRC)

        for (car in cars) {
            val carRC = Rect()
            car.getHitRect(carRC)

            if (smRC.intersect(carRC)) {
                MusicManager.instance.play(ctx, R.raw.siren_sound)
                lose()
                return true
            }
        }
        return false
    }

    private fun update() {
        val toRemove: MutableList<ImageView> = mutableListOf()

        for (s in cars) {
            s.x -= 5f + Math.sqrt(20f+score.toDouble()).toFloat()

            if (s.x <= -s.drawable.intrinsicWidth) {
                toRemove.add(s)
                score++
            }
        }

        for (s in toRemove) {
            cars.remove(s)
            view.layout.removeView(s)
        }

        view.scoreText.text = score.toString()

        if (!isLost()) {
            handler.postDelayed({ update() }, (1000 / 60).toLong())
        }
    }

    fun startFlight() {
        view.stickmanDrawable.stop()
        currentVelocity = jumpVelocity
        flightUpdate(view.stickmanView.y)
    }

    private fun flightUpdate(jumpedFrom: Float) {
        if (view.stickmanView.y - currentVelocity > jumpedFrom) {
            currentVelocity = 0
            view.stickmanView.y = jumpedFrom
            view.stickmanDrawable.start()
            return
        }
        view.stickmanView.y -= currentVelocity
        currentVelocity -= gravity
        handler.postDelayed({flightUpdate(jumpedFrom)}, 100)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = RunnerGameView()
        view.setContentView(this)

        MusicManager.instance.play(this, R.raw.runner_music)

        drawInformationDialog(getString(R.string.runner_info_title), getString(R.string.runner_info_message),
                {
                    createCar()
                    update()
                }, view.ankoContext)
    }
    override fun lose() {
        handler.removeCallbacksAndMessages(null)
        drawInformationDialog(
                getString(R.string.runner_end_title),
                getString(R.string.runner_end_message_template).format(score),
                { super.lose() },
                view.ankoContext
        )
    }
}