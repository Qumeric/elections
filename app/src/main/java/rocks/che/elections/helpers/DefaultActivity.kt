package rocks.che.elections.helpers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.otto.Bus
import im.delight.android.audio.MusicManager
import im.delight.android.audio.SoundManager
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import rocks.che.elections.R
import rocks.che.elections.logic.gamestate

abstract class DefaultActivity: AppCompatActivity(), AnkoLogger {
    private var soundManager: SoundManager? = null
    protected val bus = Bus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bus.register(this)
        MusicManager.getInstance().play(this, R.raw.main_music)
    }

	override fun onResume() {
		super.onResume()

        val maxSimultaneousStreams = 5
        soundManager = SoundManager(this, maxSimultaneousStreams)

		soundManager!!.start()
        listOf(R.raw.ah_sound, R.raw.button_sound, R.raw.catch_miss_sound, R.raw.catch_strawberry_sound,
                R.raw.choose_sound, R.raw.eat_apple_sound, R.raw.hammer_sound, R.raw.hammer_miss_sound,
                R.raw.mutin_sound, R.raw.shot_sound, R.raw.siren_sound)
                .forEach {soundManager!!.load(it)}
	}

	protected fun playSound(res: Int) {
        if (soundManager != null)
            soundManager!!.play(res)
    }

	override fun onPause() {
		super.onPause()

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
            positiveButton(R.string.yes_button) {
                run()
            }

            onCancelled {
                // FIXME make non-cancellable
                run()
            }
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (gamestate.candidate.name != "Fake") {
            Prefs.putString("gamestate", gamestate.toJSON().toString())
        }
    }
}