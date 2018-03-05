package rocks.che.elections

import android.os.Bundle
import android.os.Handler
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView

class HighlightsActivity : DefaultActivity() {
    private lateinit var view: HighlightsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = HighlightsView()
        view.setContentView(this)

        // TODO add some animation
        Handler().postDelayed({startActivity(intentFor<GameActivity>())}, 7000)
    }
}