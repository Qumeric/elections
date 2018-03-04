package rocks.che.elections

import android.support.v7.app.AppCompatActivity
import android.util.Log
import im.delight.android.audio.SoundManager
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.alert
import rocks.che.elections.logic.gamestate
import rocks.che.elections.logic.save

abstract class DefaultActivity: AppCompatActivity() {
    private var soundManager: SoundManager? = null

	override fun onResume() {
		super.onResume()

        val maxSimultaneousStreams = 5
        soundManager = SoundManager(this, maxSimultaneousStreams)

		soundManager!!.start()
        listOf(R.raw.ah_sound, R.raw.button_sound, R.raw.catch_miss_sound, R.raw.catch_strawberry_sound,
                R.raw.catcher_music, R.raw.choose_sound, R.raw.ducks_music, R.raw.eat_apple_sound,
                R.raw.hammer_miss_sound, R.raw.hammer_music, R.raw.hammer_sound, R.raw.main_music,
                R.raw.mutin_sound, R.raw.runner_music, R.raw.shot_sound, R.raw.siren_sound, R.raw.snake_music)
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
        try {
            if (gamestate.candidate.name != "Fake") {
                save(this, gamestate.toJSON().toString())
            }
        } catch (e: Exception) {
            Log.e("DefaultActivity", "Unable to saveGame")
        }
    }
}