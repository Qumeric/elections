package rocks.che.elections

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import org.jetbrains.anko.setContentView
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Answer
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.inActivityChange

const val gameRequestCode = 0

class ChangeActivity : DefaultActivity() {
    private lateinit var view: ChangeView
    private lateinit var gs: Gamestate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val answer: Answer = intent.extras.getParcelable("answer") as Answer
        gs = intent.extras.getParcelable("gamestate")

        val oldValues = gs.candidate.opinions.mapValues { it.value }
        gs.update(answer.impact)

        view = ChangeView(oldValues, gs)
        view.setContentView(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gameRequestCode && resultCode == Activity.RESULT_OK) {
            val prize = data.getIntExtra("money", 0)
            gs.money += prize

            val intent = Intent(this, PollActivity::class.java)
            intent.putExtra("gamestate", gs);
            inActivityChange = true
            startActivity(intent)
        }
    }
}