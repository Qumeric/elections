package rocks.che.elections.minigames

import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.setContentView
import rocks.che.elections.R
import rocks.che.elections.helpers.OnSwipeTouchListener
import java.util.*

class SnakeGameActivity : MiniGameActivity() {
    data class Position(val y: Int, val x: Int) {
        operator fun plus(d: Direction): Position = when (d) {
            Direction.NORTH -> Position(y - 1, x)
            Direction.SOUTH -> Position(y + 1, x)
            Direction.WEST -> Position(y, x - 1)
            Direction.EAST -> Position(y, x + 1)
        }

        fun isValid(yLimit: Int, xLimit: Int): Boolean {
            return x >= 0 && y >= 0 && x < xLimit && y < yLimit
        }
    }

    enum class Direction {
        NORTH, SOUTH, WEST, EAST
    }

    private lateinit var view: SnakeGameView

    private val startLength = 5

    private val snake: Deque<Position> = LinkedList()
    var d = Direction.EAST

    private var apple: Position? = null

    fun eat() {
        score++
        playSound(R.raw.eat_apple_sound)
        apple = genApple()
    }

    private var appleResource = R.drawable.ic_apple_1
    private fun randomAppleResource() {
        appleResource = listOf(R.drawable.ic_apple_1, R.drawable.ic_apple_2,
                R.drawable.ic_apple_3)[Random().nextInt(3)]
    }

    private fun snakeGrow(p: Position) {
        view.field[p.y][p.x].setImageResource(R.color.black)
        view.field[p.y][p.x].visibility = VISIBLE
        snake.addFirst(p)
    }

    private fun snakeShrink() {
        view.field[snake.last.y][snake.last.x].setImageResource(0)
        view.field[snake.last.y][snake.last.x].visibility = INVISIBLE
        snake.removeLast()
    }

    private fun update() {
        view.scoreText.text = score.toString()

        val nextHeadPos = snake.first + d
        Log.d("Snake", "nextHeadPos: " + nextHeadPos)
        if (!nextHeadPos.isValid(view.rowCnt, view.colCnt) || inSnake(nextHeadPos)) {
            lose()
            return
        }
        snakeGrow(nextHeadPos)

        if (snake.first != apple) {
            snakeShrink()
        } else {
            eat()
        }

        handler.postDelayed({ update() }, 150L-score)
    }

    private fun inSnake(obj: Position): Boolean = snake.any { it == obj }

    private fun genApple(): Position {
        if (apple != null) {
            view.field[apple!!.y][apple!!.x].setBackgroundResource(R.color.black)
            view.field[apple!!.y][apple!!.x].setImageResource(0)
            view.field[apple!!.y][apple!!.x].invalidate()
        }
        var apple: Position
        do {
            apple = Position(Random().nextInt(view.rowCnt), Random().nextInt(view.colCnt))
        } while (inSnake(apple))
        randomAppleResource()
        view.field[apple.y][apple.x].setBackgroundResource(R.color.green)
        view.field[apple.y][apple.x].setImageResource(appleResource)
        view.field[apple.y][apple.x].visibility = VISIBLE
        view.field[apple.y][apple.x].invalidate()
        return apple
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = SnakeGameView(object : OnSwipeTouchListener(this) {
            override fun onSwipeTop() {
                if (d != Direction.SOUTH) d = Direction.NORTH
            }

            override fun onSwipeRight() {
                if (d != Direction.WEST) d = Direction.EAST
            }

            override fun onSwipeLeft() {
                if (d != Direction.EAST) d = Direction.WEST
            }

            override fun onSwipeBottom() {
                if (d != Direction.NORTH) d = Direction.SOUTH
            }
        })
        view.setContentView(this)

        MusicManager.instance.play(this, R.raw.snake_music)

        for (i in 1..startLength) {
            snakeGrow(Position(2, i))
        }
        apple = genApple()

        drawInformationDialog(getString(R.string.snake_info_title), getString(R.string.snake_info_message),
                { update() }, view.ankoContext)
    }

    override fun lose() {
        handler.removeCallbacksAndMessages(null)
        drawInformationDialog(
                getString(R.string.snake_end_title),
                getString(R.string.snake_end_message_template).format(score),
                { super.lose() },
                view.ankoContext
        )
    }
}
