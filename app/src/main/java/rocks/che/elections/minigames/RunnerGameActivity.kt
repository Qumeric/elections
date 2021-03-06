package rocks.che.elections.minigames

import android.graphics.Rect
import android.os.Bundle
import android.widget.ImageView
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.*
import rocks.che.elections.R
import java.lang.Math.random

class RunnerGameActivity : MiniGameActivity() {
    private lateinit var view: RunnerGameView
    private val jumpVelocity = 20
    private val gravity = 4
    private var currentVelocity = 0
    var jumps: Int = 0

    private val cars: MutableSet<ImageView> = mutableSetOf()

    private fun createCar() {
        val carView = ImageView(this)
        carView.setImageResource(R.drawable.ic_police_car)

        carView.x = displayMetrics.widthPixels.toFloat()
        carView.y = dip(6).toFloat()
        view.layout.addView(carView)
        cars.add(carView)

        handler.postDelayed({ createCar() }, ((5000 * (0.7 + random())) / Math.sqrt(10.0 + score)).toLong())
    }

    private fun isLost(): Boolean {
        val smRC = Rect()
        view.stickmanView.getHitRect(smRC)

        for (car in cars) {
            val carRC = Rect()
            car.getHitRect(carRC)
            val topCarRC = carRC.apply {
                left += 20
                right -= 20
            }
            val botCarRC = carRC.apply {
                bottom = (carRC.bottom + carRC.top) / 2
                top = (carRC.bottom + carRC.top) / 2
            }

            if (smRC.intersect(topCarRC) || smRC.intersect(botCarRC)) {
                playSound(R.raw.siren_sound)
                lose()
                return true
            }
        }
        return false
    }

    private fun update() {
        val toRemove: MutableList<ImageView> = mutableListOf()

        for (s in cars) {
            s.x -= dip(250f + 10*Math.sqrt(10f + score.toDouble()).toFloat())/30

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
            handler.postDelayed({ update() }, (1000 / 50).toLong())
        }
    }

    fun startFlight() {
        view.stickmanDrawable.stop()
        if (currentVelocity < 0) {
            currentVelocity /= 2
        }
        currentVelocity = (jumpVelocity * 2.5).toInt()
        if (jumps == 0) {
            flightUpdate(view.stickmanView.y)
        }
        jumps++
    }

    private fun flightUpdate(jumpedFrom: Float) {
        if (view.stickmanView.y - currentVelocity > jumpedFrom) {
            currentVelocity = 0
            view.stickmanView.y = jumpedFrom
            view.stickmanDrawable.start()
            jumps = 0
            return
        }
        view.stickmanView.y -= currentVelocity
        currentVelocity -= gravity
        handler.postDelayed({ flightUpdate(jumpedFrom) }, 20)
    }

    private var wallsInRow = 0
    private fun buildKremlinWall(x: Float = displayMetrics.widthPixels.toFloat()) {
        var drawable = R.drawable.runner_kremlin_wall
        when {
            wallsInRow < 5 -> wallsInRow++
            wallsInRow > 20 -> {
                drawable = R.drawable.runner_kremlin_tower
                wallsInRow = 0
            }
            random() < 0.1f -> {
                drawable = R.drawable.runner_kremlin_tower
                wallsInRow = 0
            }
            else -> wallsInRow++
        }
        createMovingImage(drawable, 10f, x = x, onAppear = { buildKremlinWall(it) })
    }

    private fun buildInitialWall(x: Float = 0f) {
        if (x > displayMetrics.widthPixels) {
            createMovingImage(R.drawable.runner_kremlin_wall, 10f, x = x, onAppear = { buildKremlinWall(it) })
            return
        }
        createMovingImage(R.drawable.runner_kremlin_wall, 10f, x = x, onAppear = { buildInitialWall(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = RunnerGameView()
        view.setContentView(this)

        MusicManager.instance.play(this, R.raw.runner_music)

        useMovingImages(find(R.id.moving_image_layout))

        drawInformationDialog(getString(R.string.runner_info_title), getString(R.string.runner_info_message),
            {
                createCar()
                update()
                buildInitialWall()
            }, view.ankoContext)
    }

    override fun lose() {
        score *= 2
        handler.removeCallbacksAndMessages(null)
        drawInformationDialog(
            getString(R.string.runner_end_title),
            resources.getQuantityString(R.plurals.runner_end_message_template, score, score),
            { super.lose() },
            view.ankoContext
        )
    }
}