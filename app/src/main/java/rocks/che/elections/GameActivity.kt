package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView

class GameActivity : DefaultActivity() {
    private lateinit var view: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = GameView()
        view.setContentView(this)
    }
}