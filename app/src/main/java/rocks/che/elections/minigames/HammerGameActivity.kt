package rocks.che.elections.minigames

import android.os.Bundle
import android.view.View
import im.delight.android.audio.MusicManager
import im.delight.android.audio.SoundManager
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.setContentView
import rocks.che.elections.R
import java.util.*

class HammerGameActivity : MiniGameActivity() {
    private lateinit var view: HammerGameView
    private var missedHeads = 0

    private fun kill() {
        MusicManager.getInstance().play(this, R.raw.hammer_sound)
        score += 1
        view.scoreText.text = score.toString()
    }

    private fun removeEnemy(row: Int, col: Int){
        val elem = view.field[row][col]
        if (elem.visibility != View.INVISIBLE) {
            MusicManager.getInstance().play(this, R.raw.hammer_miss_sound)
            missedHeads++
            view.missedText.text = missedHeads.toString()
            elem.visibility = View.INVISIBLE
            elem.invalidate()
            if (missedHeads >= maxLose) {
                lose()
                return
            }
        }
        handler.postDelayed({createEnemy(row, col)}, 3000*(1+Random().nextDouble()).toLong())
    }

    private fun createEnemy(row: Int, col: Int){
        val elem = view.field[row][col]
        val r = view.pickRandomEnemyResource()
        elem.setImageResource(r)
        elem.visibility = View.VISIBLE
        elem.onClick {
            elem.visibility = View.INVISIBLE
            kill()
        }

        handler.postDelayed({removeEnemy(row, col)}, 3000*(1+Random().nextDouble()).toLong())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = HammerGameView()
        view.setContentView(this)

        drawInformationDialog(
                getString(R.string.hammer_info_title),
                getString(R.string.hammer_info_message),
                {
                    for (row in 0 until view.rowCnt) {
                        (0 until view.colCnt).forEach {
                            handler.postDelayed({createEnemy(row, it)}, (5000*Random().nextDouble()).toLong())
                        }
                    }
                },
                view.ankoContext)
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
