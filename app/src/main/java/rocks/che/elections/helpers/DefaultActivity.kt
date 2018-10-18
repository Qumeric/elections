package rocks.che.elections.helpers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import im.delight.android.audio.MusicManager
import im.delight.android.audio.SoundManager
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import rocks.che.elections.R
import rocks.che.elections.logic.inActivityChange

abstract class DefaultActivity : AppCompatActivity(), AnkoLogger {
    private var soundManager: SoundManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inActivityChange = false

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onResume() {
        super.onResume()

        MusicManager.instance.play()

        val maxSimultaneousStreams = 5
        soundManager = SoundManager(this, maxSimultaneousStreams)

        soundManager!!.start()
        listOf(R.raw.ah_sound, R.raw.button_sound, R.raw.catch_miss_sound, R.raw.catch_strawberry_sound,
            R.raw.choose_sound, R.raw.eat_apple_sound, R.raw.hammer_sound, R.raw.hammer_miss_sound,
            R.raw.duck_hit_sound, R.raw.mutin_sound, R.raw.shot_sound, R.raw.siren_sound,
            R.raw.choose_sound,
            R.raw.main_music, R.raw.ducks_music, R.raw.catcher_music,
            R.raw.runner_music, R.raw.snake_music, R.raw.races_music) // FIXME is music even required?
            .forEach { soundManager!!.load(it) }
    }

    fun playSound(res: Int) {
        if (soundManager != null)
            soundManager!!.play(res)
    }

    override fun onPause() {
        super.onPause()

        if (!inActivityChange) {
            MusicManager.instance.pause()
        }

        if (soundManager != null) {
            soundManager!!.cancel()
            soundManager = null
        }
    }

    override fun onBackPressed() {
        // Do nothing
    }

    fun drawInformationDialog(_title: String, _message: String, run: () -> Unit, ui: AnkoContext<DefaultActivity>) = with(ui) {
        alert {
            title = _title
            message = _message
            isCancelable = false
            positiveButton(R.string.yes_button) {
                run()
            }

            onCancelled {
                // FIXME make non-cancellable
                run()
            }
        }.show()
    }


}