package rocks.che.elections.minigames

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.imageResource
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

        override operator fun equals(other: Any?): Boolean {
            if (other == null || other !is Position) {
                return false
            }
            return x == other.x && y == other.y
        }

        fun isValid(yLimit: Int, xLimit: Int): Boolean {
            return x >= 0 && y >= 0 && x < xLimit && y < yLimit
        }
    }

    enum class Direction {
        NORTH, SOUTH, WEST, EAST
    }

    val appleCount = 2

    private lateinit var view: SnakeGameView

    private val startLength = 5

    private val snake: Deque<Position> = LinkedList()
    var d = Direction.EAST

    private val apples = mutableListOf<Position>()

    private fun eat(apple: Position) {
        Log.d("SnakeActivty", apples.size.toString())
        score++
        playSound(R.raw.eat_apple_sound)
        (getElem(apple.y, apple.x) as ImageView).imageResource = 0
        apples.remove(apple)
        apples.add(genApple())
    }

    private fun getElem(y: Int, x: Int) = view.layout.getChildAt(y*view.colCnt+x)

    private var appleResource = R.drawable.ic_apple_1
    private fun randomAppleResource() {
        appleResource = listOf(R.drawable.ic_apple_1, R.drawable.ic_apple_2,
            R.drawable.ic_apple_3)[Random().nextInt(3)]
    }

    private fun snakeGrow(p: Position) {
        getElem(p.y, p.x).backgroundResource = R.color.black
        snake.addFirst(p)
    }

    private fun snakeShrink() {
        getElem(snake.last.y, snake.last.x).backgroundResource = 0
        snake.removeLast()
    }

    private fun update() {
        view.scoreText.text = score.toString()

        val nextHeadPos = snake.first + d
        if (!nextHeadPos.isValid(view.rowCnt, view.colCnt) || inSnake(nextHeadPos)) {
            lose()
            return
        }
        snakeGrow(nextHeadPos)

        val eatenApple = apples.find {snake.first == it}
        if (eatenApple == null) {
            snakeShrink()
        } else {
            eat(eatenApple)
        }

        handler.postDelayed({ update() }, 150L - score)
    }

    private fun inSnake(obj: Position): Boolean = snake.any { it == obj }

    private fun genApple(): Position {
        var apple: Position
        do {
            apple = Position(Random().nextInt(view.rowCnt), Random().nextInt(view.colCnt))
        } while (inSnake(apple))
        randomAppleResource()
        (getElem(apple.y, apple.x) as ImageView).imageResource = appleResource
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
        for (i in 1..appleCount) {
            apples.add(genApple())
        }

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
