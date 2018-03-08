package rocks.che.elections

import android.os.Bundle
import im.delight.android.audio.MusicManager
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate

class NewGameActivity: DefaultActivity() {
    private lateinit var view: NewGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gamestate = Gamestate.loadGame()
        if (gamestate != null) {
            startActivity<GameActivity>("gs" to gamestate)
        }

        view = NewGameView()
        view.setContentView(this)
        MusicManager.instance.play(this, R.raw.main_music)
    }
}