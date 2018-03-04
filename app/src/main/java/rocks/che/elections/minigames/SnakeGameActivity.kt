package rocks.che.elections.minigames

import android.os.Bundle
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.setContentView
import rocks.che.elections.R
import rocks.che.elections.helpers.OnSwipeTouchListener
import java.util.*



class SnakeGameActivity : MiniGameActivity() {
    data class Position(val y: Int, val x: Int) {
        fun plus(d: Direction): Position {
            if (d == Direction.NORTH) {
                return Position(y - 1, x)
            }
            if (d == Direction.SOUTH) {
                return Position(y + 1, x)
            }
            if (d == Direction.WEST) {
                return Position(y, x - 1)
            }
            // d == Direction.EAST
            return Position(y, x + 1)
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

    private lateinit var apple: Position

    fun eat() {
        score++
        apple = genApple()
    }

    private val update = object : Runnable {
        override fun run() {
            view.scoreText.text = score.toString()

            view.field
                    .flatMap { it }
                    .forEach { it.backgroundResource = R.color.green }

            val nextHeadPos = snake.first.plus(d)
            if (!nextHeadPos.isValid(view.rowCnt, view.colCnt) || inSnake(nextHeadPos)) {
                lose()
                return
            }
            snake.addFirst(nextHeadPos)

            if (snake.first != apple) {
                snake.removeLast()
            } else {
                eat()
            }

            for (pos in snake) {
                val elem = view.field[pos.y][pos.x]
                elem.backgroundResource = R.color.navy
            }

            view.field[apple.y][apple.x].backgroundResource = R.color.red

            handler.postDelayed(this, 500L)
        }
    }

    private fun inSnake(obj: Position): Boolean {
        return snake.any { it == obj }
    }

    private fun genApple(): Position {
        var apple: Position
        do {
            apple = Position(Random().nextInt(view.rowCnt), Random().nextInt(view.colCnt))
        } while (inSnake(apple))
        return apple
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = SnakeGameView(object : OnSwipeTouchListener(this) {
                override fun onSwipeTop() {
                    if (d != Direction.SOUTH) {
                        d = Direction.NORTH
                    }
                }

                override fun onSwipeRight() {
                    if (d != Direction.WEST) {
                        d = Direction.EAST
                    }
                }

                override fun onSwipeLeft() {
                    if (d != Direction.EAST) {
                        d = Direction.WEST
                    }
                }

                override fun onSwipeBottom() {
                    if (d != Direction.NORTH) {
                        d = Direction.SOUTH
                    }
                }
            }
        )
        view.setContentView(this)

        for (i in 1..startLength) {
            snake.addLast(Position(0, startLength - i))
        }
        apple = genApple()

        drawInformationDialog(getString(R.string.snake_info_title), getString(R.string.snake_info_message),
                {
                    handler.postDelayed(update, 1)
                }, view.ankoContext)
    }
    override fun lose() {
        drawInformationDialog(
                getString(R.string.snake_end_title),
                getString(R.string.snake_end_message_template).format(score),
                { super.lose() },
                view.ankoContext
        )
    }
}
