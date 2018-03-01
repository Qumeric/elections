package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView

class NewGameActivity: DefaultActivity() {
    private lateinit var view: NewGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = NewGameView()
        view.setContentView(this)
    }
}