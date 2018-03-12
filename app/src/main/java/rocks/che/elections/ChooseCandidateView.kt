package rocks.che.elections

import android.graphics.Typeface
import android.support.v4.widget.TextViewCompat
import android.view.Gravity
import android.widget.GridLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
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
                padding = dip(12)

                val candidates = loadCandidates(resources)
                for (candidate in candidates) {
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
                                                text = "%s: %d".format(group, value)
                                            }
                                        }
                                    }
                                }.lparams {
                                    weight = 0.8f
                                    height = 0
                                    width = matchParent
                                }

                                val b = button {
                                    text = candidate.name
                                    typeface = Typeface.createFromAsset(ctx.assets, "mfred.ttf")
                                    textSize = 20f
                                    if (candidate.resource != R.drawable.candidate_navalny || secretUnlocked) {
                                        backgroundResource = R.color.blue
                                        onClick {
                                            inActivityChange = true
                                            ctx.startActivity<HighlightsActivity>("gamestate" to Gamestate(candidate,
                                                    candidates as MutableList<Candidate>, loadQuestions(resources)))
                                        }
                                    } else {
                                        backgroundResource = R.color.gray
                                        onClick {
                                            snackbar(this@verticalLayout, R.string.need_unblock)
                                        }
                                    }
                                }.lparams {
                                    width = matchParent
                                    height = 0
                                    weight = 0.2f
                                }
                                TextViewCompat.setAutoSizeTextTypeWithDefaults(b, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
                            }.lparams(width = matchParent, height = matchParent)
                            if (candidate.resource == R.drawable.candidate_navalny && !secretUnlocked) {
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
