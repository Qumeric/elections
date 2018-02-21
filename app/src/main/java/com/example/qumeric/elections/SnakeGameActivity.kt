package com.example.qumeric.elections

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.setContentView
import java.util.*

data class Position(val y: Int, val x: Int) {
    fun plus(d: Direction): Position {
        if (d == Direction.NORTH) {
            return Position(y-1, x)
        }
        if (d == Direction.SOUTH) {
            return Position(y+1, x)
        }
        if (d == Direction.WEST) {
            return Position(y, x-1)
        }
        // d == Direction.EAST
        return Position(y, x+1)
    }
    fun equals(p: Position): Boolean {
        return (x == p.x && y == p.y)
    }
    fun isInvalid(yLimit: Int, xLimit: Int): Boolean {
        return (x >= 0 && y >= 0 && x < xLimit && y < yLimit)
    }
}

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

class SnakeGameActivity : AppCompatActivity() {
    private lateinit var view: SnakeGameView

    private var score = 0
    val snake: Deque<Position> = LinkedList()
    var apple: Position = Position(10, 10)
    var d: Direction = Direction.EAST

    fun eat() {
        score += 1
        apple = genApple()
    }

    val handler = Handler();

    val update = object : Runnable {
        override fun run() {
            view.scoreText.text = score.toString()

            for (row in view.field) {
                for (elem in row) {
                    elem.backgroundResource = R.color.grass
                }
            }

            snake.addFirst(snake.first.plus(d))
            if (snake.first.isInvalid(view.rowCnt, view.colCnt)) {
                lose()
                return
            }
            if (!snake.first.equals(apple)) {
                snake.removeLast()
            } else {
                eat()
            }

            for (pos in snake) {
                val elem = view.field[pos.y][pos.x]
                elem.backgroundResource = R.color.darkBlue
            }

            view.field[apple.y][apple.x].backgroundResource = R.color.red

            handler.postDelayed(this, 500L)
        }
    }

    private fun inSnake(obj: Position): Boolean {
        for (pos in snake) {
            if (pos.equals(obj)) {
                return true
            }
        }
        return false
    }

    private fun genApple(): Position {
        var apple: Position
        do {
            apple = Position(Random().nextInt(view.rowCnt), Random().nextInt(view.colCnt))
        } while(inSnake(apple))
        return apple
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = SnakeGameView()
        view.setContentView(this)
        snake.addLast(Position(0, 0));

        apple = genApple();
        handler.postDelayed(update, 1)
    }

    fun lose() {
        Log.d("SnakeGameActivity", "LOSE")
    }

}
