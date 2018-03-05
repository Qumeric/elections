package rocks.che.elections

import android.view.Gravity
import android.widget.GridLayout
import com.squareup.otto.Bus
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.GamestateUpdate
import rocks.che.elections.helpers.cardView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.Candidate
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.loadCandidates
import rocks.che.elections.logic.loadQuestions

class ChooseCandidateView(val secretUnlocked: Boolean = false, val bus: Bus = Bus()) : DefaultView<ChooseCandidateActivity> {
    private lateinit var ankoContext: AnkoContext<ChooseCandidateActivity>

    override fun createView(ui: AnkoContext<ChooseCandidateActivity>) = with(ui) {
        ankoContext = ui

        var row = 0
        var col = 0

        verticalLayout {
            weightSum = 1f
            gravity = Gravity.CENTER
            verticalLayout {
                gravity = Gravity.CENTER
                gameTextView(20) {
                    textResource = R.string.choose_candidate
                }
            }.lparams {
                height = matchParent
                weight = 0.9f
            }

            gridLayout {
                rowCount = 3
                columnCount = 2
                alignmentMode = GridLayout.ALIGN_MARGINS
                padding = dip(12)

                for (candidate in loadCandidates(resources)) {
                    cardView {
                        frameLayout {
                            verticalLayout {
                                gravity = Gravity.CENTER

                                linearLayout {
                                    gravity = Gravity.CENTER

                                    imageView {
                                        imageResource = candidate.resource
                                    }.lparams {
                                        width = dip(80)
                                    }

                                    verticalLayout {
                                        gravity = Gravity.END
                                        for ((group, value) in candidate.opinions) {
                                            gameTextView {
                                                text = "%s: %d".format(group, value.value)
                                            }
                                        }
                                    }
                                }.lparams {
                                    weight = 0.8f
                                    height = 0
                                    width = matchParent
                                }

                                button(candidate.name) {
                                    backgroundResource = R.color.blue
                                    onClick {
                                        bus.post(GamestateUpdate(Gamestate(candidate, loadQuestions(resources),
                                                loadCandidates(resources) as MutableList<Candidate>)))
                                    }
                                }.lparams {
                                    width = matchParent
                                    height = 0
                                    weight = 0.2f
                                }
                            }.lparams(width = matchParent, height = matchParent)
                            if (candidate.name == ctx.getString(R.string.secret_candidate_name) && !secretUnlocked) {
                                view {
                                    backgroundResource = R.color.black
                                    alpha = 0.5f
                                }.lparams(width = matchParent, height = matchParent)
                                imageView {
                                    imageResource = R.drawable.ic_locked
                                }.lparams {
                                    width = dip(50)
                                    height = dip(50)
                                    gravity = Gravity.CENTER
                                }
                            }
                        }
                    }.lparams {
                        width = 0
                        height = 0
                        leftMargin = dip(8)
                        rightMargin = dip(8)
                        bottomMargin = dip(16)
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                    }
                    col++
                    if (col == 2) {
                        col = 0
                        row++
                    }
                }
            }.lparams {
                width = matchParent
                height = matchParent
                weight = 0.1f
            }
        }
    }
}
