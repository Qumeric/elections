package rocks.che.elections.debate

import android.os.Bundle
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.setContentView
import rocks.che.elections.DefaultActivity

class DebateActivity : DefaultActivity() {
    private lateinit var view: AnkoComponent<DebateActivity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = DebateViewStart()
        view.setContentView(this)
    }
}