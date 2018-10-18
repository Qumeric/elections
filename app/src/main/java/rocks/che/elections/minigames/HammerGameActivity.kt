package rocks.che.elections.minigames

import android.os.Bundle
import android.view.View
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.setContentView
import rocks.che.elections.R
import rocks.che.elections.helpers.scaleView
import java.util.*
import kotlin.math.sqrt

class HammerGameActivity : MiniGameActivity() {
    private lateinit var view: HammerGameView
    private var missedHeads = 0

    private fun kill() {
        playSound(R.raw.hammer_sound)
        score += 1
        view.scoreText.text = score.toString()
    }

    private fun removeEnemy(row: Int, col: Int) {
        val elem = view.field[row][col]
        if (elem.visibility != View.INVISIBLE) {
            playSound(R.raw.hammer_miss_sound)
            missedHeads++
            view.missedText.text = missedHeads.toString()
            elem.visibility = View.INVISIBLE
            elem.invalidate()
            if (missedHeads >= maxLose) {
                lose()
                return
            }
        }
        val timeToHide = (5000 * (1 + Random().nextDouble()) / sqrt(5 + score.toDouble())).toLong()
        handler.postDelayed({ createEnemy(row, col) }, timeToHide)
    }

    private fun createEnemy(row: Int, col: Int) {
        val elem = view.field[row][col]
        val r = view.pickRandomEnemyResource()
        elem.setImageResource(r)
        elem.visibility = View.VISIBLE
        elem.onClick {
            elem.clearAnimation()
            elem.visibility = View.INVISIBLE
            kill()
        }

        val timeToStay = (7000 * (1 + Random().nextDouble()) / sqrt(5 + score.toDouble())).toLong()
        scaleView(elem, 1f, 0.2f, duration = timeToStay, pivotY = 1f)
        handler.postDelayed({ removeEnemy(row, col) }, timeToStay)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = HammerGameView()
        view.setContentView(this)
        MusicManager.instance.play(this, R.raw.hammer_music)

        drawInformationDialog(
            getString(R.string.hammer_info_title),
            getString(R.string.hammer_info_message),
            {
                for (row in 0 until view.rowCnt) {
                    (0 until view.colCnt).forEach {
                        handler.postDelayed({ createEnemy(row, it) }, (5000 * Random().nextDouble()).toLong())
                    }
                }
            },
            view.ankoContext)
    }

    override fun lose() {
        handler.removeCallbacksAndMessages(null)
        drawInformationDialog(
            getString(R.string.hammer_end_title),
            getString(R.string.hammer_end_message_template).format(score),
            { super.lose() },
            view.ankoContext
        )
    }
}
