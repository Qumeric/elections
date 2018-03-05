package rocks.che.elections

import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Answer
import rocks.che.elections.logic.gamestate

class ChangeActivity : DefaultActivity() {
    private lateinit var view: ChangeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val answer: Answer = intent.extras.getSerializable("answer") as Answer

        val oldValues = gamestate.opinions.mapValues { it.value.value }
        answer.select()

        view = ChangeView(oldValues)
        view.setContentView(this)
    }
}