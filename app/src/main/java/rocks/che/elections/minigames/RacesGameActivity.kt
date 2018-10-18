package rocks.che.elections.minigames

import android.os.Bundle
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.*
import rocks.che.elections.R
import rocks.che.elections.helpers.scaleView
import rocks.che.elections.logic.Candidate
import java.lang.Math.random
import kotlin.math.min
import kotlin.math.sqrt

class RacesGameActivity : MiniGameActivity() {
    private lateinit var view: RacesGameView

    private var speed = 0f
    private var acceleration = 0f
    private var deceleration = 0f
    private val speeds = mutableListOf(0f, 0f, 0f, 0f, 0f, 0f)

    fun tap() {
        speed += acceleration
        score++
        view.scoreText.text = score.toString()
    }

    private fun update() {
        speed = maxOf(0f, speed - deceleration)
        view.horse.x += speed

        if (view.horse.x >= displayMetrics.widthPixels - view.horse.width) {
            win()
            return
        }

        for ((index, h) in view.opponentHorses.withIndex()) {
            speeds[index] += dip(sqrt(min(sqrt(0.1 / random()).toFloat(), 20f))).toFloat() / 1.8f
            speeds[index] = maxOf(0f, speeds[index] - deceleration)
            h.x += dip(speeds[index])
            if (h.x >= displayMetrics.widthPixels - h.width) {
                score = (score*0.75).toInt()
                lose()
                return
            }
        }

        handler.postDelayed({ update() }, 1000 / 50)
    }

    private fun onStartNumber(number: String) {
        val tv = view.preText
        tv.text = number
        tv.textColor = R.color.red
        scaleView(tv, 1f, 0f, duration = 1000)
    }

    private fun start() {
        onStartNumber("3")
        handler.postDelayed({ onStartNumber("2") }, 1000)
        handler.postDelayed({ onStartNumber("1") }, 2000)

        handler.postDelayed({
            view.started = true
            update()
        }, 3000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val candidates: ArrayList<Candidate> = intent.getParcelableArrayListExtra("candidates")
        view = RacesGameView(candidates)
        view.setContentView(this)

        acceleration = dip(45f) / 30f
        deceleration = dip(10f) / 30f

        useMovingImages(find(R.id.moving_image_layout))
        MusicManager.instance.play(this, R.raw.races_music)

        drawInformationDialog(getString(R.string.ladder_info_title), getString(R.string.ladder_info_message),
            { start() }, view.ankoContext)
    }

    fun win() {
        score = (score * 1.25).toInt()
        lose()
    }

    override fun lose() {
        handler.removeCallbacksAndMessages(null)
        drawInformationDialog(
            getString(R.string.ladder_end_title),
            getString(R.string.ladder_end_message_template).format(score),
            { super.lose() },
            view.ankoContext
        )
    }
}
