package rocks.che.elections

import android.util.Log
import android.view.Gravity
import android.widget.GridLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import rocks.che.elections.helpers.DefaultView
import rocks.che.elections.helpers.cardView
import rocks.che.elections.helpers.gameTextView
import rocks.che.elections.logic.*

class ChooseCandidateView(val secretUnlocked: Boolean = false) : DefaultView<ChooseCandidateActivity> {
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
                padding = dip(14)

                for (candidate in loadCandidates(resources)) {
                    cardView {
                        cardElevation = dip(8).toFloat()
                        radius = 0f

                        verticalLayout {
                            gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL

                            linearLayout {
                                gravity = Gravity.CENTER

                                imageView {
                                    imageResource = candidate.resource
                                }.lparams {
                                    width = dip(80)
                                }

                                verticalLayout {
                                    for ((group, value) in candidate.opinions) {
                                        gameTextView(8) {
                                            text = "%s: %d".format(group, value.value)
                                        }
                                    }
                                }
                            }.lparams {
                                height = 0
                                weight = 0.8f
                            }

                            themedButton(theme = R.style.button) {
                                text = candidate.name
                                if (candidate.name != ctx.getString(R.string.secret_candidate_name) || secretUnlocked) {
                                    backgroundResource = R.color.blue
                                } else {
                                    Log.d("ChooseCandidateView", "there is a secret candidate and he's blocked")
                                    backgroundResource = R.color.silver
                                }
                                onClick {
                                    gamestate = Gamestate(candidate, loadQuestions(resources),
                                            loadCandidates(resources) as MutableList<Candidate>)
                                    ctx.startActivity<HighlightsActivity>()
                                }
                            }.lparams {
                                width = matchParent
                                height = 0
                                weight = 0.2f
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
