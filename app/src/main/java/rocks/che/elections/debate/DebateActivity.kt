package rocks.che.elections.debate

import android.os.Bundle
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView
import rocks.che.elections.DefaultActivity
import rocks.che.elections.PollActivity
import rocks.che.elections.logic.gamestate

class DebateActivity : DefaultActivity() {
    private val views = mutableListOf<AnkoComponent<DebateActivity>>()
    private var stage = 0
    val groupDistribution = mutableMapOf<String, Int>()
    val opponentDistribution = mutableMapOf<String, Int>()
    val maxMinutes = 60
    var minutes = 60

    fun setGroupDistribution(vals: List<Int>) {
        var cVal = 0
        for ((group, _) in gamestate.questions) {
            groupDistribution[group] = vals[cVal++]
        }
    }

    fun setOpponentDistribution(vals: List<Int>) {
        var cVal = 0
        for (candidate in gamestate.candidates) {
            opponentDistribution[candidate.name] = vals[cVal++]
        }
    }

    fun nextStage() {
        if (stage+1 == views.size) {
            startActivity(intentFor<PollActivity>())
            // FIXME assign scores
            return
        }
        views[++stage].setContentView(this)
    }

    fun winGroup(): String {
        return "Business" // FIXME
    }

    fun loseGroup(): String {
        return "Media" // FIXME
    }

    fun attackResult(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        views.add(DebateViewStart())
        views.add(DebateViewChoose())
        views.add(DebateViewGroups())
        views.add(DebateViewOpponents())
        views.add(DebateViewEnd())
        views[stage].setContentView(this)
    }
}