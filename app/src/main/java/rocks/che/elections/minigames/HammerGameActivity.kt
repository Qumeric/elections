package rocks.che.elections.minigames

import android.os.Bundle
import android.view.View
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.setContentView
import rocks.che.elections.R

class HammerGameActivity : MiniGameActivity() {
    private lateinit var view: HammerGameView

    private lateinit var gonnaShow: Array<BooleanArray>

    fun kill(imgResource: Int) {
        score += 1
    }

    fun createEnemy(row: Int, col: Int): Runnable {
        val elem = view.field[row][col]
        return Runnable {
            val r = view.pickRandomEnemyResource()
            elem.setImageResource(r)
            elem.visibility = View.VISIBLE
            elem.onClick {
                elem.visibility = View.INVISIBLE
                kill(r)
                gonnaShow[row][col] = false
            }
        }
    }

    val update = object : Runnable {
        override fun run() {
            view.scoreText.text = score.toString()

            for (row in 0 until view.rowCnt) {
                for (col in 0 until view.colCnt) {
                    if (view.field[row][col].visibility == View.INVISIBLE && !gonnaShow[row][col]) {
                        gonnaShow[row][col] = true
                        handler.postDelayed(createEnemy(row, col), (1000 / (10 + score)).toLong())
                    }
                }
            }

            handler.postDelayed(this, (1000 / 50).toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = HammerGameView()
        view.setContentView(this)

        gonnaShow = Array(view.rowCnt, { booleanArrayOf() })
        for (row in 0 until view.rowCnt) {
            val rowValue = BooleanArray(view.colCnt)
            gonnaShow[row] = rowValue
        }


        drawInformationDialog(getString(R.string.hammer_info_title), getString(R.string.hammer_info_message),
                {
                    handler.postDelayed(update, 1)
                }, view.ankoContext)

    }
}
