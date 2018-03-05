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

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        Handler().postDelayed({startActivity(intentFor<GameActivity>())}, 7000)
    }
}